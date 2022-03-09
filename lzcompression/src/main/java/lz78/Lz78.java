package lz78;

import utils.BitInputStream;
import utils.BitOutputStream;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static utils.Constants.*;

public class Lz78 {

    public static void compress(InputStream inputStream, OutputStream outputStream) {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream);
             BitOutputStream bos = new BitOutputStream(outputStream)) {
            StringBuilder lookAheadBuff = new StringBuilder(BUFFER_SIZE);
            Map<Integer, String> dict = new HashMap<>(WINDOW_SIZE);
            int index = 1;
            while(bis.available() > 0 || lookAheadBuff.length() > 1) {
                while(bis.available() > 0 && lookAheadBuff.length() < BUFFER_SIZE) {
                    lookAheadBuff.append((char) bis.read());
                }
                char s = '\0';
                int i = 0;
                for (int k = lookAheadBuff.length()-1; k > 0; k--) {
                    String substr = lookAheadBuff.substring(0, k);
                    i = indexOfMap(dict, substr);
                    if (i == -1) {
                        if (k == 1) {
                            s = popChar(lookAheadBuff, 0);
                            i = 0;
                            dict.put(index, String.valueOf(s));
                            break;
                        }
                        continue;
                    }
                    s = popChar(lookAheadBuff, substr.length());
                    dict.put(index, substr + s);
                    break;
                }
                index = index % WINDOW_SIZE + 1;
                bos.write(i, WINDOW_SIZE_BIT_COUNT);
                bos.write((byte) s);
            }
            if (lookAheadBuff.length() > 0) {
                char s = lookAheadBuff.charAt(0);
                int i = indexOfMap(dict, String.valueOf(s));
                if (i != -1) {
                    bos.write(i, WINDOW_SIZE_BIT_COUNT);
                    bos.write((byte) 0);
                } else {
                    bos.write((byte) 0, WINDOW_SIZE_BIT_COUNT);
                    bos.write((byte) s);
                }

            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decode(InputStream inputStream, OutputStream outputStream) {
        try (BitInputStream bis = new BitInputStream(inputStream);
             BufferedOutputStream bos = new BufferedOutputStream(outputStream, WINDOW_SIZE)) {
            HashMap<Integer, String> dict = new HashMap<>(WINDOW_SIZE);
            int index = 1;
            while (bis.available() > 0) {
                int i = bis.read(WINDOW_SIZE_BIT_COUNT);
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
                    index = index % WINDOW_SIZE + 1;
                    continue;
                }
                dict.put(index, String.valueOf((char) s));
                bos.write((byte) s);
                index = index % WINDOW_SIZE + 1;
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static char popChar(StringBuilder buff, int index) {
        char s = buff.charAt(index);
        buff.delete(0, index+1);
        return s;
    }

    private static int indexOfMap(Map<Integer, String> map, String value) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return -1;
    }

}
