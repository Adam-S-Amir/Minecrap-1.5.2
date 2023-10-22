/*
 * Decompiled with CFR 0.152.
 */
package SevenZip.Compression.LZMA;

import SevenZip.Compression.RangeCoder.Decoder;
import java.io.IOException;

class Decoder.LiteralDecoder.Decoder2 {
    short[] m_Decoders = new short[768];

    Decoder.LiteralDecoder.Decoder2() {
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
