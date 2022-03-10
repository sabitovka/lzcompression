package io.github.sabkar.datacompression.compression.algorithms.lzss;

import io.github.sabkar.datacompression.compression.Decoder;
import io.github.sabkar.datacompression.compression.algorithms.BaseAlgorithm;
import io.github.sabkar.datacompression.compression.utils.streams.BitInputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LZSSDecoder extends BaseAlgorithm implements Decoder {

    public LZSSDecoder(int dictBitCount, int bufferBitCount) {
        super(dictBitCount, bufferBitCount);
    }

    public LZSSDecoder() {
        super();
    }

    @Override
    public void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
        BitInputStream bis = new BitInputStream(inputStream);
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        StringBuilder dict = new StringBuilder(this.dictSize+1);

        for (int i = 0; i < this.dictSize; i++) {
            dict.append('\0');
        }

        while (bis.available() > 0) {
            int flag = bis.read();
            if (flag == -1) {
                break;
            }
            if (flag > 0) {
                int offset = bis.read(this.dictBitCount);
                int matchLength = bis.read(this.bufferBitCount);
                if (offset == -1 || matchLength == -1) {
                    return;
                }
                for (byte i = 0; i < matchLength; i++) {
                    char s = dict.charAt(offset);
                    appendToDict(dict, String.valueOf(s));
                    bos.write(s);
                }
                continue;
            }
            int s = bis.read(8);
            if (s != -1) {
                appendToDict(dict, String.valueOf((char) s));
                bos.write(s);
            }
        }

        bis.close();
        bos.close();
    }

}
