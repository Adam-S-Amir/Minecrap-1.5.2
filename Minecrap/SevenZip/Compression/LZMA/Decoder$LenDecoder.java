/*
 * Decompiled with CFR 0.152.
 */
package SevenZip.Compression.LZMA;

import SevenZip.Compression.RangeCoder.BitTreeDecoder;
import SevenZip.Compression.RangeCoder.Decoder;
import java.io.IOException;

class Decoder.LenDecoder {
    short[] m_Choice = new short[2];
    BitTreeDecoder[] m_LowCoder = new BitTreeDecoder[16];
    BitTreeDecoder[] m_MidCoder = new BitTreeDecoder[16];
    BitTreeDecoder m_HighCoder = new BitTreeDecoder(8);
    int m_NumPosStates = 0;

    Decoder.LenDecoder() {
    }

    public void Create(int numPosStates) {
        while (this.m_NumPosStates < numPosStates) {
            this.m_LowCoder[this.m_NumPosStates] = new BitTreeDecoder(3);
            this.m_MidCoder[this.m_NumPosStates] = new BitTreeDecoder(3);
            ++this.m_NumPosStates;
        }
    }

    public void Init() {
        Decoder.InitBitModels(this.m_Choice);
        for (int posState = 0; posState < this.m_NumPosStates; ++posState) {
            this.m_LowCoder[posState].Init();
            this.m_MidCoder[posState].Init();
        }
        this.m_HighCoder.Init();
    }

    public int Decode(Decoder rangeDecoder, int posState) throws IOException {
        if (rangeDecoder.DecodeBit(this.m_Choice, 0) == 0) {
            return this.m_LowCoder[posState].Decode(rangeDecoder);
        }
        int symbol = 8;
        symbol = rangeDecoder.DecodeBit(this.m_Choice, 1) == 0 ? (symbol += this.m_MidCoder[posState].Decode(rangeDecoder)) : (symbol += 8 + this.m_HighCoder.Decode(rangeDecoder));
        return symbol;
    }
}
