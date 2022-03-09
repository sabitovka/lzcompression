package io.sabkar.lzcompression.algorithms.lz78;

import io.sabkar.lzcompression.Compressor;
import io.sabkar.lzcompression.algorithms.BaseAlgorithm;
import io.sabkar.lzcompression.utils.CustomHashMap;
import io.sabkar.lzcompression.utils.streams.BitOutputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LZ78Compressor extends BaseAlgorithm implements Compressor {

    public LZ78Compressor(int dictBitCount, int bufferBitCount) {
        super(dictBitCount, bufferBitCount);
    }

    public LZ78Compressor() {
        super();
    }

    @Override
    public void compress(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        BitOutputStream bos = new BitOutputStream(outputStream);

        StringBuilder lookAheadBuff = new StringBuilder(this.bufferSize);
        CustomHashMap<Integer, String> dict = new CustomHashMap<>(this.dictSize);

        int index = 1;
        while(bis.available() > 0 || lookAheadBuff.length() > 1) {
            while(bis.available() > 0 && lookAheadBuff.length() < this.bufferSize) {
                lookAheadBuff.append((char) bis.read());
            }
            char s = '\0';
            Integer i = 0;
            for (int k = lookAheadBuff.length()-1; k > 0; k--) {
                String substr = lookAheadBuff.substring(0, k);
                i = dict.getKeyByValue(substr);
                if (i == null) {
                    if (k == 1) {
                        s = popChar(lookAheadBuff, 0);
                        i = 0;
                        dict.put(index, String.valueOf(s));
                        break;
                    }
                    continue;
                }
                s = popChar(lookAheadBuff, substr.length());
                dict.put(index, substr + s);
                break;
            }
            index = index % this.dictSize + 1;
            bos.write(i, this.dictBitCount);
            bos.write((byte) s);
        }
        if (lookAheadBuff.length() > 0) {
            char s = lookAheadBuff.charAt(0);
            int i = dict.getKeyByValue(String.valueOf(s));
            if (i != -1) {
                bos.write(i, this.dictBitCount);
                bos.write((byte) 0);
            } else {
                bos.write((byte) 0, this.dictBitCount);
                bos.write((byte) s);
            }

        }
        bos.close();
        bis.close();
    }

}
