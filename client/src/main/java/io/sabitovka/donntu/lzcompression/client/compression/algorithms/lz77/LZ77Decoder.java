package io.sabitovka.donntu.lzcompression.client.compression.algorithms.lz77;


import io.sabitovka.donntu.lzcompression.client.compression.Decoder;
import io.sabitovka.donntu.lzcompression.client.compression.algorithms.BaseAlgorithm;

import java.io.*;

public class LZ77Decoder extends BaseAlgorithm implements Decoder {

    public LZ77Decoder(int dictBitCount, int bufferBitCount) {
        super(dictBitCount, bufferBitCount);
    }

    public LZ77Decoder() {
        super();
    }

    @Override
    public void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);

        StringBuilder dict = new StringBuilder(this.dictSize+1);
        for (int i = 0; i < this.dictSize; i++) {
            dict.append('\0');
        }

        //TODO: Поставить на работу с BitInputStream
        while (bis.available() > 0) {
            int offsetMatchLength = bis.read();
            if (offsetMatchLength == -1) {
                break;
            }
            byte offset = (byte) (offsetMatchLength >> 3);
            byte matchLength = (byte) (offsetMatchLength & ((1 << 3)-1));
            for (int i = 0; i < matchLength; i++) {
                char s = dict.charAt(offset);
                appendToDict(dict, String.valueOf(s));
                bos.write(s);
            }
            char s = (char) bis.read();
            appendToDict(dict, String.valueOf(s));
            bos.write(s);
        }

        bis.close();
        bos.close();
    }

}
