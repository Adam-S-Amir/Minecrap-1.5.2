// 
// Decompiled by Procyon v0.5.36
// 

package SevenZip.Compression.LZMA;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import SevenZip.Compression.RangeCoder.BitTreeDecoder;
import SevenZip.Compression.LZ.OutWindow;

public class Decoder
{
    OutWindow m_OutWindow;
    SevenZip.Compression.RangeCoder.Decoder m_RangeDecoder;
    short[] m_IsMatchDecoders;
    short[] m_IsRepDecoders;
    short[] m_IsRepG0Decoders;
    short[] m_IsRepG1Decoders;
    short[] m_IsRepG2Decoders;
    short[] m_IsRep0LongDecoders;
    BitTreeDecoder[] m_PosSlotDecoder;
    short[] m_PosDecoders;
    BitTreeDecoder m_PosAlignDecoder;
    LenDecoder m_LenDecoder;
    LenDecoder m_RepLenDecoder;
    LiteralDecoder m_LiteralDecoder;
    int m_DictionarySize;
    int m_DictionarySizeCheck;
    int m_PosStateMask;
    
    public Decoder() {
        this.m_OutWindow = new OutWindow();
        this.m_RangeDecoder = new SevenZip.Compression.RangeCoder.Decoder();
        this.m_IsMatchDecoders = new short[192];
        this.m_IsRepDecoders = new short[12];
        this.m_IsRepG0Decoders = new short[12];
        this.m_IsRepG1Decoders = new short[12];
        this.m_IsRepG2Decoders = new short[12];
        this.m_IsRep0LongDecoders = new short[192];
        this.m_PosSlotDecoder = new BitTreeDecoder[4];
        this.m_PosDecoders = new short[114];
        this.m_PosAlignDecoder = new BitTreeDecoder(4);
        this.m_LenDecoder = new LenDecoder();
        this.m_RepLenDecoder = new LenDecoder();
        this.m_LiteralDecoder = new LiteralDecoder();
        this.m_DictionarySize = -1;
        this.m_DictionarySizeCheck = -1;
        for (int i = 0; i < 4; ++i) {
            this.m_PosSlotDecoder[i] = new BitTreeDecoder(6);
        }
    }
    
    boolean SetDictionarySize(final int dictionarySize) {
        if (dictionarySize < 0) {
            return false;
        }
        if (this.m_DictionarySize != dictionarySize) {
            this.m_DictionarySize = dictionarySize;
            this.m_DictionarySizeCheck = Math.max(this.m_DictionarySize, 1);
            this.m_OutWindow.Create(Math.max(this.m_DictionarySizeCheck, 4096));
        }
        return true;
    }
    
    boolean SetLcLpPb(final int lc, final int lp, final int pb) {
        if (lc > 8 || lp > 4 || pb > 4) {
            return false;
        }
        this.m_LiteralDecoder.Create(lp, lc);
        final int numPosStates = 1 << pb;
        this.m_LenDecoder.Create(numPosStates);
        this.m_RepLenDecoder.Create(numPosStates);
        this.m_PosStateMask = numPosStates - 1;
        return true;
    }
    
    void Init() throws IOException {
        this.m_OutWindow.Init(false);
        SevenZip.Compression.RangeCoder.Decoder.InitBitModels(this.m_IsMatchDecoders);
        SevenZip.Compression.RangeCoder.Decoder.InitBitModels(this.m_IsRep0LongDecoders);
        SevenZip.Compression.RangeCoder.Decoder.InitBitModels(this.m_IsRepDecoders);
        SevenZip.Compression.RangeCoder.Decoder.InitBitModels(this.m_IsRepG0Decoders);
        SevenZip.Compression.RangeCoder.Decoder.InitBitModels(this.m_IsRepG1Decoders);
        SevenZip.Compression.RangeCoder.Decoder.InitBitModels(this.m_IsRepG2Decoders);
        SevenZip.Compression.RangeCoder.Decoder.InitBitModels(this.m_PosDecoders);
        this.m_LiteralDecoder.Init();
        for (int i = 0; i < 4; ++i) {
            this.m_PosSlotDecoder[i].Init();
        }
        this.m_LenDecoder.Init();
        this.m_RepLenDecoder.Init();
        this.m_PosAlignDecoder.Init();
        this.m_RangeDecoder.Init();
    }
    
    public boolean Code(final InputStream inStream, final OutputStream outStream, final long outSize) throws IOException {
        this.m_RangeDecoder.SetStream(inStream);
        this.m_OutWindow.SetStream(outStream);
        this.Init();
        int state = Base.StateInit();
        int rep0 = 0;
        int rep2 = 0;
        int rep3 = 0;
        int rep4 = 0;
        long nowPos64 = 0L;
        byte prevByte = 0;
        while (outSize < 0L || nowPos64 < outSize) {
            final int posState = (int)nowPos64 & this.m_PosStateMask;
            if (this.m_RangeDecoder.DecodeBit(this.m_IsMatchDecoders, (state << 4) + posState) == 0) {
                final LiteralDecoder.Decoder2 decoder2 = this.m_LiteralDecoder.GetDecoder((int)nowPos64, prevByte);
                if (!Base.StateIsCharState(state)) {
                    prevByte = decoder2.DecodeWithMatchByte(this.m_RangeDecoder, this.m_OutWindow.GetByte(rep0));
                }
                else {
                    prevByte = decoder2.DecodeNormal(this.m_RangeDecoder);
                }
                this.m_OutWindow.PutByte(prevByte);
                state = Base.StateUpdateChar(state);
                ++nowPos64;
            }
            else {
                int len;
                if (this.m_RangeDecoder.DecodeBit(this.m_IsRepDecoders, state) == 1) {
                    len = 0;
                    if (this.m_RangeDecoder.DecodeBit(this.m_IsRepG0Decoders, state) == 0) {
                        if (this.m_RangeDecoder.DecodeBit(this.m_IsRep0LongDecoders, (state << 4) + posState) == 0) {
                            state = Base.StateUpdateShortRep(state);
                            len = 1;
                        }
                    }
                    else {
                        int distance;
                        if (this.m_RangeDecoder.DecodeBit(this.m_IsRepG1Decoders, state) == 0) {
                            distance = rep2;
                        }
                        else {
                            if (this.m_RangeDecoder.DecodeBit(this.m_IsRepG2Decoders, state) == 0) {
                                distance = rep3;
                            }
                            else {
                                distance = rep4;
                                rep4 = rep3;
                            }
                            rep3 = rep2;
                        }
                        rep2 = rep0;
                        rep0 = distance;
                    }
                    if (len == 0) {
                        len = this.m_RepLenDecoder.Decode(this.m_RangeDecoder, posState) + 2;
                        state = Base.StateUpdateRep(state);
                    }
                }
                else {
                    rep4 = rep3;
                    rep3 = rep2;
                    rep2 = rep0;
                    len = 2 + this.m_LenDecoder.Decode(this.m_RangeDecoder, posState);
                    state = Base.StateUpdateMatch(state);
                    final int posSlot = this.m_PosSlotDecoder[Base.GetLenToPosState(len)].Decode(this.m_RangeDecoder);
                    if (posSlot >= 4) {
                        final int numDirectBits = (posSlot >> 1) - 1;
                        rep0 = (0x2 | (posSlot & 0x1)) << numDirectBits;
                        if (posSlot < 14) {
                            rep0 += BitTreeDecoder.ReverseDecode(this.m_PosDecoders, rep0 - posSlot - 1, this.m_RangeDecoder, numDirectBits);
                        }
                        else {
                            rep0 += this.m_RangeDecoder.DecodeDirectBits(numDirectBits - 4) << 4;
                            rep0 += this.m_PosAlignDecoder.ReverseDecode(this.m_RangeDecoder);
                            if (rep0 < 0) {
                                if (rep0 == -1) {
                                    break;
                                }
                                return false;
                            }
                        }
                    }
                    else {
                        rep0 = posSlot;
                    }
                }
                if (rep0 >= nowPos64 || rep0 >= this.m_DictionarySizeCheck) {
                    return false;
                }
                this.m_OutWindow.CopyBlock(rep0, len);
                nowPos64 += len;
                prevByte = this.m_OutWindow.GetByte(0);
            }
        }
        this.m_OutWindow.Flush();
        this.m_OutWindow.ReleaseStream();
        this.m_RangeDecoder.ReleaseStream();
        return true;
    }
    
    public boolean SetDecoderProperties(final byte[] properties) {
        if (properties.length < 5) {
            return false;
        }
        final int val = properties[0] & 0xFF;
        final int lc = val % 9;
        final int remainder = val / 9;
        final int lp = remainder % 5;
        final int pb = remainder / 5;
        int dictionarySize = 0;
        for (int i = 0; i < 4; ++i) {
            dictionarySize += (properties[1 + i] & 0xFF) << i * 8;
        }
        return this.SetLcLpPb(lc, lp, pb) && this.SetDictionarySize(dictionarySize);
    }
    
    class LenDecoder
    {
        short[] m_Choice;
        BitTreeDecoder[] m_LowCoder;
        BitTreeDecoder[] m_MidCoder;
        BitTreeDecoder m_HighCoder;
        int m_NumPosStates;
        
        LenDecoder() {
            this.m_Choice = new short[2];
            this.m_LowCoder = new BitTreeDecoder[16];
            this.m_MidCoder = new BitTreeDecoder[16];
            this.m_HighCoder = new BitTreeDecoder(8);
            this.m_NumPosStates = 0;
        }
        
        public void Create(final int numPosStates) {
            while (this.m_NumPosStates < numPosStates) {
                this.m_LowCoder[this.m_NumPosStates] = new BitTreeDecoder(3);
                this.m_MidCoder[this.m_NumPosStates] = new BitTreeDecoder(3);
                ++this.m_NumPosStates;
            }
        }
        
        public void Init() {
            SevenZip.Compression.RangeCoder.Decoder.InitBitModels(this.m_Choice);
            for (int posState = 0; posState < this.m_NumPosStates; ++posState) {
                this.m_LowCoder[posState].Init();
                this.m_MidCoder[posState].Init();
            }
            this.m_HighCoder.Init();
        }
        
        public int Decode(final SevenZip.Compression.RangeCoder.Decoder rangeDecoder, final int posState) throws IOException {
            if (rangeDecoder.DecodeBit(this.m_Choice, 0) == 0) {
                return this.m_LowCoder[posState].Decode(rangeDecoder);
            }
            int symbol = 8;
            if (rangeDecoder.DecodeBit(this.m_Choice, 1) == 0) {
                symbol += this.m_MidCoder[posState].Decode(rangeDecoder);
            }
            else {
                symbol += 8 + this.m_HighCoder.Decode(rangeDecoder);
            }
            return symbol;
        }
    }
    
    class LiteralDecoder
    {
        Decoder2[] m_Coders;
        int m_NumPrevBits;
        int m_NumPosBits;
        int m_PosMask;
        
        public void Create(final int numPosBits, final int numPrevBits) {
            if (this.m_Coders != null && this.m_NumPrevBits == numPrevBits && this.m_NumPosBits == numPosBits) {
                return;
            }
            this.m_NumPosBits = numPosBits;
            this.m_PosMask = (1 << numPosBits) - 1;
            this.m_NumPrevBits = numPrevBits;
            final int numStates = 1 << this.m_NumPrevBits + this.m_NumPosBits;
            this.m_Coders = new Decoder2[numStates];
            for (int i = 0; i < numStates; ++i) {
                this.m_Coders[i] = new Decoder2();
            }
        }
        
        public void Init() {
            for (int numStates = 1 << this.m_NumPrevBits + this.m_NumPosBits, i = 0; i < numStates; ++i) {
                this.m_Coders[i].Init();
            }
        }
        
        Decoder2 GetDecoder(final int pos, final byte prevByte) {
            return this.m_Coders[((pos & this.m_PosMask) << this.m_NumPrevBits) + ((prevByte & 0xFF) >>> 8 - this.m_NumPrevBits)];
        }
        
        class Decoder2
        {
            short[] m_Decoders;
            
            Decoder2() {
                this.m_Decoders = new short[768];
            }
            
            public void Init() {
                SevenZip.Compression.RangeCoder.Decoder.InitBitModels(this.m_Decoders);
            }
            
            public byte DecodeNormal(final SevenZip.Compression.RangeCoder.Decoder rangeDecoder) throws IOException {
                int symbol = 1;
                do {
                    symbol = (symbol << 1 | rangeDecoder.DecodeBit(this.m_Decoders, symbol));
                } while (symbol < 256);
                return (byte)symbol;
            }
            
            public byte DecodeWithMatchByte(final SevenZip.Compression.RangeCoder.Decoder rangeDecoder, byte matchByte) throws IOException {
                int symbol = 1;
                do {
                    final int matchBit = matchByte >> 7 & 0x1;
                    matchByte <<= 1;
                    final int bit = rangeDecoder.DecodeBit(this.m_Decoders, (1 + matchBit << 8) + symbol);
                    symbol = (symbol << 1 | bit);
                    if (matchBit != bit) {
                        while (symbol < 256) {
                            symbol = (symbol << 1 | rangeDecoder.DecodeBit(this.m_Decoders, symbol));
                        }
                        break;
                    }
                } while (symbol < 256);
                return (byte)symbol;
            }
        }
    }
}
