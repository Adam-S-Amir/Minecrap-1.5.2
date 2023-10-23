// 
// Decompiled by Procyon v0.5.36
// 

package SevenZip.Compression.LZ;

import java.io.IOException;
import java.io.OutputStream;

public class OutWindow
{
    byte[] _buffer;
    int _pos;
    int _windowSize;
    int _streamPos;
    OutputStream _stream;
    
    public OutWindow() {
        this._windowSize = 0;
    }
    
    public void Create(final int windowSize) {
        if (this._buffer == null || this._windowSize != windowSize) {
            this._buffer = new byte[windowSize];
        }
        this._windowSize = windowSize;
        this._pos = 0;
        this._streamPos = 0;
    }
    
    public void SetStream(final OutputStream stream) throws IOException {
        this.ReleaseStream();
        this._stream = stream;
    }
    
    public void ReleaseStream() throws IOException {
        this.Flush();
        this._stream = null;
    }
    
    public void Init(final boolean solid) {
        if (!solid) {
            this._streamPos = 0;
            this._pos = 0;
        }
    }
    
    public void Flush() throws IOException {
        final int size = this._pos - this._streamPos;
        if (size == 0) {
            return;
        }
        this._stream.write(this._buffer, this._streamPos, size);
        if (this._pos >= this._windowSize) {
            this._pos = 0;
        }
        this._streamPos = this._pos;
    }
    
    public void CopyBlock(final int distance, int len) throws IOException {
        int pos = this._pos - distance - 1;
        if (pos < 0) {
            pos += this._windowSize;
        }
        while (len != 0) {
            if (pos >= this._windowSize) {
                pos = 0;
            }
            this._buffer[this._pos++] = this._buffer[pos++];
            if (this._pos >= this._windowSize) {
                this.Flush();
            }
            --len;
        }
    }
    
    public void PutByte(final byte b) throws IOException {
        this._buffer[this._pos++] = b;
        if (this._pos >= this._windowSize) {
            this.Flush();
        }
    }
    
    public byte GetByte(final int distance) {
        int pos = this._pos - distance - 1;
        if (pos < 0) {
            pos += this._windowSize;
        }
        return this._buffer[pos];
    }
}
