package io.github.sabkar.datacompression.compression.algorithms.lzss;

import io.github.sabkar.datacompression.compression.Compressor;
import io.github.sabkar.datacompression.compression.algorithms.BaseAlgorithm;
import io.github.sabkar.datacompression.compression.utils.streams.BitOutputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LZSSCompressor extends BaseAlgorithm implements Compressor {

    public LZSSCompressor(int dictBitCount, int bufferBitCount) {
        super(dictBitCount, bufferBitCount);
    }

    public LZSSCompressor() {
        super();
    }

    @Override
    public void compress(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        BitOutputStream bos = new BitOutputStream(outputStream);

        StringBuilder lookAheadBuff = new StringBuilder(this.bufferSize);
        StringBuilder dict = new StringBuilder(this.dictSize+1);

        for (int i = 0; i < this.dictSize; i++) {
            dict.append('\0');
        }

        while (bis.available() > 0 || lookAheadBuff.length() > 1) {
            while (bis.available() > 0 && lookAheadBuff.length() < this.bufferSize) {
                lookAheadBuff.append((char) bis.read());
            }
            boolean f = false;
            byte i = 0, j = 0;
            char s = '\0';
            for (int k = lookAheadBuff.length()-1; k > 0; k--) {
                String substr = lookAheadBuff.substring(0, k);
                int index = dict.indexOf(substr);
                if (index == -1) {
                    if (k == 1) {
                        s = popChar(lookAheadBuff, 0);
                        appendToDict(dict, substr);
                        break;
                    }
                    continue;
                }
                i = (byte) index;
                j = (byte) substr.length();
                s = popChar(lookAheadBuff, j-1);
                f = j > 1;
                appendToDict(dict, substr);
                break;
            }
            bos.write(f);
            if (f) {
                bos.write(i, this.dictBitCount);
                bos.write(j, this.bufferBitCount);
                continue;
            }
            bos.write(s);

        }

        if (lookAheadBuff.length() > 0) {
            char s = lookAheadBuff.charAt(0);
            bos.write(false);
            bos.write(s);
        }

        bos.close();
        bis.close();

    }

}