/*
 * Decompiled with CFR 0.152.
 */
package SevenZip.Compression.LZMA;

import SevenZip.Compression.RangeCoder.Decoder;
import java.io.IOException;

class Decoder.LiteralDecoder {
    Decoder2[] m_Coders;
    int m_NumPrevBits;
    int m_NumPosBits;
    int m_PosMask;

    Decoder.LiteralDecoder() {
    }

    public void Create(int numPosBits, int numPrevBits) {
        if (this.m_Coders != null && this.m_NumPrevBits == numPrevBits && this.m_NumPosBits == numPosBits) {
            return;
        }
        this.m_NumPosBits = numPosBits;
        this.m_PosMask = (1 << numPosBits) - 1;
        this.m_NumPrevBits = numPrevBits;
        int numStates = 1 << this.m_NumPrevBits + this.m_NumPosBits;
        this.m_Coders = new Decoder2[numStates];
        for (int i = 0; i < numStates; ++i) {
            this.m_Coders[i] = new Decoder2();
        }
    }

    public void Init() {
        int numStates = 1 << this.m_NumPrevBits + this.m_NumPosBits;
        for (int i = 0; i < numStates; ++i) {
            this.m_Coders[i].Init();
        }
    }

    Decoder2 GetDecoder(int pos, byte prevByte) {
        return this.m_Coders[((pos & this.m_PosMask) << this.m_NumPrevBits) + ((prevByte & 0xFF) >>> 8 - this.m_NumPrevBits)];
    }

    class Decoder2 {
        short[] m_Decoders = new short[768];

        Decoder2() {
        }

        public void Init() {
            Decoder.InitBitModels(this.m_Decoders);
        }

        public byte DecodeNormal(Decoder rangeDecoder) throws IOException {
            int symbol = 1;
            while ((symbol = symbol << 1 | rangeDecoder.DecodeBit(this.m_Decoders, symbol)) < 256) {
            }
            return (byte)symbol;
        }

        public byte DecodeWithMatchByte(Decoder rangeDecoder, byte matchByte) throws IOException {
            int symbol = 1;
            do {
                int matchBit = matchByte >> 7 & 1;
                matchByte = (byte)(matchByte << 1);
                int bit = rangeDecoder.DecodeBit(this.m_Decoders, (1 + matchBit << 8) + symbol);
                symbol = symbol << 1 | bit;
                if (matchBit == bit) continue;
                while (symbol < 256) {
                    symbol = symbol << 1 | rangeDecoder.DecodeBit(this.m_Decoders, symbol);
                }
                break;
            } while (symbol < 256);
            return (byte)symbol;
        }
    }
}
