package io.github.sabkar.datacompression.compression.algorithms.lzw;

import io.github.sabkar.datacompression.compression.Decoder;
import io.github.sabkar.datacompression.compression.algorithms.BaseAlgorithm;
import io.github.sabkar.datacompression.compression.utils.streams.BitInputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static io.github.sabkar.datacompression.compression.utils.Constants.*;

public class LZWDecoder extends BaseAlgorithm implements Decoder {

    @Override
    public void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
        BitInputStream bis = new BitInputStream(inputStream);
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);

        Map<Integer, String> table = new HashMap<>();

        for (int i = 0; i < TABLE_OFFSET; i++) {
            table.put(i, String.valueOf((char) i));
        }

        int code = bis.read(MAX_TABLE_BIT_LENGTH);
        if (code == -1) {
            bis.close();
            bos.close();
            return;
        }
        String inputPhrase = table.get(code);

        int index = 0;
        while (true) {
            for (int i = 0; i < inputPhrase.length(); i++) {
                bos.write(inputPhrase.charAt(i));
            }
            code = bis.read(MAX_TABLE_BIT_LENGTH);
            if (code == -1) {
                bis.close();
                bos.close();
                return;
            }
            String s = table.get(code);
            if ((TABLE_OFFSET + index) == code) {
                s = inputPhrase + inputPhrase.charAt(0);
            }
            table.put((TABLE_OFFSET + index), inputPhrase + s.charAt(0));
            index = (index+1) % (MAX_TABLE_LENGTH - TABLE_OFFSET);
            inputPhrase = s;
        }
    }

}
