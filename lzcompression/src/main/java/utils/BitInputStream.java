package utils;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream implements AutoCloseable {

    private InputStream in;

    private int currentByte;

    private byte currentBit = 0;

    public BitInputStream(InputStream in) {
        this.in = in;
    }

    public int read() throws IOException {
        if (currentBit <= 0) {
            currentByte = in.read();
            if (currentByte == -1) {
                return -1;
            }
            currentBit = 8;
        }
        return ((currentByte & (1 << currentBit-1)) >> --currentBit);
    }

    public int read(int n) throws IOException {
        int res = 0;
        for (int i = 0; i < n; i++) {
            int bit = this.read();
            if (bit == -1) {
                return -1;
            }
            res = (res << 1) | bit;
        }
        return res;
    }

    public int available() throws IOException {
        return in.available() * 8 + currentBit;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
