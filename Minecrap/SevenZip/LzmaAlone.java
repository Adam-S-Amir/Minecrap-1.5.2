// 
// Decompiled by Procyon v0.5.36
// 

package SevenZip;

import SevenZip.Compression.LZMA.Decoder;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;

public class LzmaAlone
{
    public static void decompress(final File in, final File out) throws Exception {
        final File inFile = in;
        final File outFile = out;
        final BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(inFile));
        final BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(outFile));
        final int propertiesSize = 5;
        final byte[] properties = new byte[propertiesSize];
        if (inStream.read(properties, 0, propertiesSize) != propertiesSize) {
            throw new Exception("input .lzma file is too short");
        }
        final Decoder decoder = new Decoder();
        if (!decoder.SetDecoderProperties(properties)) {
            throw new Exception("Incorrect stream properties");
        }
        long outSize = 0L;
        for (int i = 0; i < 8; ++i) {
            final int v = inStream.read();
            if (v < 0) {
                throw new Exception("Can't read stream size");
            }
            outSize |= (long)v << 8 * i;
        }
        if (!decoder.Code(inStream, outStream, outSize)) {
            throw new Exception("Error in data stream");
        }
        outStream.flush();
        outStream.close();
        inStream.close();
    }
}
