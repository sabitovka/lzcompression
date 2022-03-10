package io.github.sabkar.datacompression.compression.algorithms.lzw;

import io.github.sabkar.datacompression.compression.Compressor;
import io.github.sabkar.datacompression.compression.utils.CustomHashMap;
import io.github.sabkar.datacompression.compression.utils.streams.BitOutputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static io.github.sabkar.datacompression.compression.utils.Constants.*;

public class LZWCompressor implements Compressor {

    @Override
    public void compress(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        BitOutputStream bos = new BitOutputStream(outputStream);

        CustomHashMap<Integer, String> table = new CustomHashMap<>(MAX_TABLE_LENGTH);

        for (int i = 0; i < TABLE_OFFSET; i++) {
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
            bos.write(table.getKeyByValue(prevSymbol), MAX_TABLE_BIT_LENGTH);
            prevSymbol = String.valueOf((char) symbol);
            table.put(TABLE_OFFSET + index, currentPhrase);
            index = (index+1) % (MAX_TABLE_LENGTH - TABLE_OFFSET);
        }

        if (!prevSymbol.isEmpty()) {
            bos.write(table.getKeyByValue(prevSymbol), MAX_TABLE_BIT_LENGTH);
        }

        bis.close();
        bos.close();

    }

}
