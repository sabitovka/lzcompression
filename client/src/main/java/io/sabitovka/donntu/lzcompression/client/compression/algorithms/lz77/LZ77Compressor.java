package io.sabitovka.donntu.lzcompression.client.compression.algorithms.lz77;

import io.sabitovka.donntu.lzcompression.client.compression.Compressor;
import io.sabitovka.donntu.lzcompression.client.compression.algorithms.BaseAlgorithm;

import java.io.*;

public class LZ77Compressor extends BaseAlgorithm implements Compressor {

    public LZ77Compressor(int dictBitCount, int bufferBitCount) {
        super(dictBitCount, bufferBitCount);
    }

    public LZ77Compressor() {
        super();
    }

    @Override
    public void compress(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);

        StringBuilder lookAheadBuff = new StringBuilder(this.bufferSize);
        StringBuilder dict = new StringBuilder(this.dictSize+1);

        for (int i = 0; i < this.dictSize; i++) {
            dict.append('\0');
        }

        while (bis.available() > 0 || lookAheadBuff.length() > 1) {
            while (bis.available() > 0 && lookAheadBuff.length() < this.bufferSize) {
                lookAheadBuff.append((char) bis.read());
            }
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
                s = popChar(lookAheadBuff, k);
                appendToDict(dict, substr + s);
                break;
            }
            //TODO: Переделать на работу с BitOutputStream
            bos.write(new byte[] { (byte) ((i << 3) + j), (byte) s, });
        }
        if (lookAheadBuff.length() > 0) {
            byte i = 0, j = 0;
            char s = lookAheadBuff.charAt(0);
            bos.write(new byte[] { (byte) ((i << 3) + j), (byte) s, });
        }
        bos.close();
        bis.close();
    }

}
