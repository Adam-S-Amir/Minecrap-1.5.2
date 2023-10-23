// 
// Decompiled by Procyon v0.5.36
// 

package SevenZip.Compression.RangeCoder;

import java.io.IOException;

public class BitTreeDecoder
{
    short[] Models;
    int NumBitLevels;
    
    public BitTreeDecoder(final int numBitLevels) {
        this.NumBitLevels = numBitLevels;
        this.Models = new short[1 << numBitLevels];
    }
    
    public void Init() {
        Decoder.InitBitModels(this.Models);
    }
    
    public int Decode(final Decoder rangeDecoder) throws IOException {
        int m = 1;
        for (int bitIndex = this.NumBitLevels; bitIndex != 0; --bitIndex) {
            m = (m << 1) + rangeDecoder.DecodeBit(this.Models, m);
        }
        return m - (1 << this.NumBitLevels);
    }
    
    public int ReverseDecode(final Decoder rangeDecoder) throws IOException {
        int m = 1;
        int symbol = 0;
        for (int bitIndex = 0; bitIndex < this.NumBitLevels; ++bitIndex) {
            final int bit = rangeDecoder.DecodeBit(this.Models, m);
            m <<= 1;
            m += bit;
            symbol |= bit << bitIndex;
        }
        return symbol;
    }
    
    public static int ReverseDecode(final short[] Models, final int startIndex, final Decoder rangeDecoder, final int NumBitLevels) throws IOException {
        int m = 1;
        int symbol = 0;
        for (int bitIndex = 0; bitIndex < NumBitLevels; ++bitIndex) {
            final int bit = rangeDecoder.DecodeBit(Models, startIndex + m);
            m <<= 1;
            m += bit;
            symbol |= bit << bitIndex;
        }
        return symbol;
    }
}
