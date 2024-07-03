package io.sabitovka.donntu.lzcompression.client.compression.algorithms.lzw;

import io.sabitovka.donntu.lzcompression.client.compression.Compressor;
import io.sabitovka.donntu.lzcompression.client.compression.utils.CustomHashMap;
import io.sabitovka.donntu.lzcompression.client.compression.utils.streams.BitOutputStream;
import io.sabitovka.donntu.lzcompression.client.compression.utils.Constants;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LZWCompressor implements Compressor {

    @Override
    public void compress(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        BitOutputStream bos = new BitOutputStream(outputStream);

        CustomHashMap<Integer, String> table = new CustomHashMap<>(Constants.MAX_TABLE_LENGTH);

        for (int i = 0; i < Constants.TABLE_OFFSET; i++) {
            table.put(i, String.valueOf((char) i));
        }

        String prevSymbol = "";

        int index = 0;
        while (bis.available() > 0) {
            int symbol = bis.read();
            String currentPhrase = prevSymbol + (symbol == -1 ? "" : (char) symbol);
            if (table.containsValue(currentPhrase)) {
                prevSymbol = currentPhrase;
                continue;
            }
            bos.write(table.getKeyByValue(prevSymbol), Constants.MAX_TABLE_BIT_LENGTH);
            prevSymbol = String.valueOf((char) symbol);
            table.put(Constants.TABLE_OFFSET + index, currentPhrase);
            index = (index+1) % (Constants.MAX_TABLE_LENGTH - Constants.TABLE_OFFSET);
        }

        if (!prevSymbol.isEmpty()) {
            bos.write(table.getKeyByValue(prevSymbol), Constants.MAX_TABLE_BIT_LENGTH);
        }

        bis.close();
        bos.close();

    }

}
