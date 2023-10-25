// 
// Decompiled by Procyon v0.5.36
// 

package SevenZip.Compression.LZMA;

public class Base
{
    public static final int kNumRepDistances = 4;
    public static final int kNumStates = 12;
    public static final int kNumPosSlotBits = 6;
    public static final int kDicLogSizeMin = 0;
    public static final int kNumLenToPosStatesBits = 2;
    public static final int kNumLenToPosStates = 4;
    public static final int kMatchMinLen = 2;
    public static final int kNumAlignBits = 4;
    public static final int kAlignTableSize = 16;
    public static final int kAlignMask = 15;
    public static final int kStartPosModelIndex = 4;
    public static final int kEndPosModelIndex = 14;
    public static final int kNumPosModels = 10;
    public static final int kNumFullDistances = 128;
    public static final int kNumLitPosStatesBitsEncodingMax = 4;
    public static final int kNumLitContextBitsMax = 8;
    public static final int kNumPosStatesBitsMax = 4;
    public static final int kNumPosStatesMax = 16;
    public static final int kNumPosStatesBitsEncodingMax = 4;
    public static final int kNumPosStatesEncodingMax = 16;
    public static final int kNumLowLenBits = 3;
    public static final int kNumMidLenBits = 3;
    public static final int kNumHighLenBits = 8;
    public static final int kNumLowLenSymbols = 8;
    public static final int kNumMidLenSymbols = 8;
    public static final int kNumLenSymbols = 272;
    public static final int kMatchMaxLen = 273;
    
    public static final int StateInit() {
        return 0;
    }
    
    public static final int StateUpdateChar(final int index) {
        if (index < 4) {
            return 0;
        }
        if (index < 10) {
            return index - 3;
        }
        return index - 6;
    }
    
    public static final int StateUpdateMatch(final int index) {
        return (index < 7) ? 7 : 10;
    }
    
    public static final int StateUpdateRep(final int index) {
        return (index < 7) ? 8 : 11;
    }
    
    public static final int StateUpdateShortRep(final int index) {
        return (index < 7) ? 9 : 11;
    }
    
    public static final boolean StateIsCharState(final int index) {
        return index < 7;
    }
    
    public static final int GetLenToPosState(int len) {
        len -= 2;
        if (len < 4) {
            return len;
        }
        return 3;
    }
}
