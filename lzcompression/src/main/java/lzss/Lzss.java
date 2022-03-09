package lzss;

import utils.BitInputStream;
import utils.BitOutputStream;

import java.io.*;

import static utils.Constants.*;

public class Lzss {

    public static void compress(InputStream inputStream, OutputStream outputStream) {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream);
             BitOutputStream bos = new BitOutputStream(outputStream)) {
            StringBuilder lookAheadBuff = new StringBuilder(BUFFER_SIZE);
            StringBuilder dict = new StringBuilder(WINDOW_SIZE);
            for (int i = 0; i < WINDOW_SIZE; i++) {
                dict.append('\0');
            }
            while (bis.available() > 0 || lookAheadBuff.length() > 1) {
                while (bis.available() > 0 && lookAheadBuff.length() < BUFFER_SIZE) {
                    lookAheadBuff.append((char) bis.read());
                }
                boolean f = false;
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
                    s = popChar(lookAheadBuff, j-1);
                    f = j > 1;
                    appendToDict(dict, substr);
                    break;
                }
                bos.write(f);
                if (f) {
                    bos.write(i, WINDOW_SIZE_BIT_COUNT);
                    bos.write(j, BUFFER_SIZE_BIT_COUNT);
                    continue;
                }
                bos.write(s);

            }
            if (lookAheadBuff.length() > 0) {
                char s = lookAheadBuff.charAt(0);
                bos.write(false);
                bos.write(s);
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decode(InputStream inputStream, OutputStream outputStream) {
        try (BitInputStream bis = new BitInputStream(inputStream);
             BufferedOutputStream bos = new BufferedOutputStream(outputStream)) {
            StringBuilder dict = new StringBuilder(WINDOW_SIZE);
            for (int i = 0; i < WINDOW_SIZE; i++) {
                dict.append('\0');
            }
            while (bis.available() > 0) {
                int flag = bis.read();
                if (flag == -1) {
                    break;
                }
                if (flag > 0) {
                    int offset = bis.read(WINDOW_SIZE_BIT_COUNT);
                    int matchLength = bis.read(BUFFER_SIZE_BIT_COUNT);
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static char popChar(StringBuilder buff, int index) {
        char s = buff.charAt(index);
        buff.delete(0, index+1);
        return s;
    }

    private static void appendToDict(StringBuilder dict, String s) {
        dict.append(s);
        int len;
        if ((len = dict.length()) > WINDOW_SIZE) {
            dict.delete(0, len - WINDOW_SIZE);
        }
    }

}
