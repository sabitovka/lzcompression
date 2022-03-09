package io.sabkar.lzcompression.utils.streams;

import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream implements AutoCloseable {

    private OutputStream out;

    private int currentByte;

    private byte bitCount = 0;

    public BitOutputStream(OutputStream out) {
        this.out = out;
    }

    public void write(boolean bit) throws IOException {
        currentByte = currentByte << 1 | (bit ? 1 : 0);
        if (++bitCount >= 8) {
            out.write(currentByte);
            bitCount = 0;
            currentByte = 0;
        }
    }

    public void write(int aByte, int n) throws IOException {
        for (int i = n-1; i >= 0; i--) {
            this.write(((aByte >> i) & 1) > 0);
        }
    }

    public void write(int aByte) throws IOException {
        this.write(aByte, 8);
    }

    public void flush() throws IOException {
        while (bitCount != 0) {
            this.write(false);
        }
    }

    @Override
    public void close() throws IOException {
        this.flush();
        out.close();
    }

}
