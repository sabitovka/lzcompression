package io.github.sabkar.datacompression.compression.algorithms.lz78;

import io.github.sabkar.datacompression.compression.Decoder;
import io.github.sabkar.datacompression.compression.algorithms.BaseAlgorithm;
import io.github.sabkar.datacompression.compression.utils.streams.BitInputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class LZ78Decoder extends BaseAlgorithm implements Decoder {

    public LZ78Decoder(int dictBitCount, int bufferBitCount) {
        super(dictBitCount, bufferBitCount);
    }

    public LZ78Decoder() {
        super();
    }

    @Override
    public void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
        BitInputStream bis = new BitInputStream(inputStream);
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);

        HashMap<Integer, String> dict = new HashMap<>(this.dictSize);

        int index = 1;
        while (bis.available() > 0) {
            int i = bis.read(this.dictBitCount);
            int s = bis.read(8);
            if (i == -1 || s == -1) {
                break;
            }
            if (i > 0) {
                String str = dict.get(i) + ((char) s);
                dict.put(index, str);
                for (int j = 0; j < str.length(); j++) {
                    bos.write(str.charAt(j));
                }
                index = index % this.dictSize + 1;
                continue;
            }
            dict.put(index, String.valueOf((char) s));
            bos.write((byte) s);
            index = index % this.dictSize + 1;
        }
        bos.close();
        bis.close();
    }

}
