package io.sabkar.lzcompression;

import io.sabkar.lzcompression.algorithms.lzw.LZWCompressor;
import io.sabkar.lzcompression.algorithms.lzw.LZWDecoder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class Launcher {

    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("C:\\Users\\adm-sabitovka\\Desktop\\1.txt");
        FileOutputStream fos = new FileOutputStream("C:\\Users\\adm-sabitovka\\Desktop\\2.txt");

        Compressor compressor = new LZWCompressor();

        compressor.compress(fis, fos);
        //Lzw.compress(fis, fos);

        fis.close();
        fos.close();

        /*FileInputStream*/ fis = new FileInputStream("C:\\Users\\adm-sabitovka\\Desktop\\2.txt");
        /*FileOutputStream*/ fos = new FileOutputStream("C:\\Users\\adm-sabitovka\\Desktop\\3.txt");

        Decoder decoder = new LZWDecoder();
        decoder.decode(fis, fos);

        //Lzw.decode(fis, fos);
        fis.close();
        fos.close();
    }
}
