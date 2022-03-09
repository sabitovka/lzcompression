package io.sabkar.lzcompression.algorithms.lzw;

import io.sabkar.lzcompression.utils.CustomHashMap;
import io.sabkar.lzcompression.utils.streams.BitInputStream;
import io.sabkar.lzcompression.utils.streams.BitOutputStream;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Lzw {

    private static final int MAX_TABLE_BIT_LENGTH = 9;
    private static final int MAX_TABLE_LENGTH = 1 << MAX_TABLE_BIT_LENGTH;
    private static final int TABLE_OFFSET = 256;

    public static void compress(InputStream inputStream, OutputStream outputStream) {
        try(BufferedInputStream bis = new BufferedInputStream(inputStream);
            BitOutputStream bos = new BitOutputStream(outputStream)) {

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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void decode(InputStream inputStream, OutputStream outputStream) {
        try(BitInputStream bis = new BitInputStream(inputStream);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream)) {

            Map<Integer, String> table = new HashMap<>();

            for (int i = 0; i < TABLE_OFFSET; i++) {
                table.put(i, String.valueOf((char) i));
            }

            int code = bis.read(MAX_TABLE_BIT_LENGTH);
            if (code == -1) {
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
