package io.sabkar.lzcompression.algorithms.lz77;

import java.io.*;

public final class Lz77 {

    // 31 байт максимальный размер окна
    private final static int MAX_WINDOW_SIZE = (1 << 5) - 1;

    // 7 байт размер буфера обратного просмотра
    private final static int LOOK_AHEAD_BUFFER_SIZE = (1 << 3) - 1;

    public static void compress(InputStream inputStream, OutputStream outputStream) {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream);
             BufferedOutputStream bos = new BufferedOutputStream(outputStream, 8)) {
            StringBuilder lookAheadBuff = new StringBuilder(LOOK_AHEAD_BUFFER_SIZE);
            StringBuilder dict = new StringBuilder(MAX_WINDOW_SIZE);
            for (int i = 0; i < MAX_WINDOW_SIZE; i++) {
                dict.append('\0');
            }
            while (bis.available() > 0 || lookAheadBuff.length() > 1) {
                while (bis.available() > 0 && lookAheadBuff.length() < LOOK_AHEAD_BUFFER_SIZE) {
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
                //(n & (7 << 3)) >> 3
                bos.write(new byte[] { (byte) ((i << 3) + j), (byte) s, });
            }
            if (lookAheadBuff.length() > 0) {
                byte i = 0, j = 0;
                char s = lookAheadBuff.charAt(0);
                bos.write(new byte[] { (byte) ((i << 3) + j), (byte) s, });
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decode(InputStream inputStream, OutputStream outputStream) {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream)) {
            StringBuilder dict = new StringBuilder(MAX_WINDOW_SIZE);
            for (int i = 0; i < MAX_WINDOW_SIZE; i++) {
                dict.append('\0');
            }
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void appendToDict(StringBuilder dict, String s) {
        dict.append(s);
        int len;
        if ((len = dict.length()) > MAX_WINDOW_SIZE) {
            dict.delete(0, len - MAX_WINDOW_SIZE);
        }
    }

    private static char popChar(StringBuilder buff, int index) {
        char s = buff.charAt(index);
        buff.delete(0, index+1);
        return s;
    }

}
