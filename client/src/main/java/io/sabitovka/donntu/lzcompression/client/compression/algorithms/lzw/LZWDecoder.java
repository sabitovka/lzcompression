package io.sabitovka.donntu.lzcompression.client.compression.algorithms.lzw;

import io.sabitovka.donntu.lzcompression.client.compression.Decoder;
import io.sabitovka.donntu.lzcompression.client.compression.algorithms.BaseAlgorithm;
import io.sabitovka.donntu.lzcompression.client.compression.utils.streams.BitInputStream;
import io.sabitovka.donntu.lzcompression.client.compression.utils.Constants;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class LZWDecoder extends BaseAlgorithm implements Decoder {

    @Override
    public void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
        BitInputStream bis = new BitInputStream(inputStream);
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);

        Map<Integer, String> table = new HashMap<>();

        for (int i = 0; i < Constants.TABLE_OFFSET; i++) {
            table.put(i, String.valueOf((char) i));
        }

        int code = bis.read(Constants.MAX_TABLE_BIT_LENGTH);
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
            code = bis.read(Constants.MAX_TABLE_BIT_LENGTH);
            if (code == -1) {
                bis.close();
                bos.close();
                return;
            }
            String s = table.get(code);
            if ((Constants.TABLE_OFFSET + index) == code) {
                s = inputPhrase + inputPhrase.charAt(0);
            }
            table.put((Constants.TABLE_OFFSET + index), inputPhrase + s.charAt(0));
            index = (index+1) % (Constants.MAX_TABLE_LENGTH - Constants.TABLE_OFFSET);
            inputPhrase = s;
        }
    }

}
