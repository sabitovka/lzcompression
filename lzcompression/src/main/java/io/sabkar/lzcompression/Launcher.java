package io.sabkar.lzcompression;

import io.sabkar.lzcompression.algorithms.lz78.LZ78Compressor;
import io.sabkar.lzcompression.algorithms.lz78.LZ78Decoder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class Launcher {

    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("C:\\Users\\adm-sabitovka\\Desktop\\1.txt");
        FileOutputStream fos = new FileOutputStream("C:\\Users\\adm-sabitovka\\Desktop\\2.txt");

        Compressor compressor = new LZ78Compressor();

        compressor.compress(fis, fos);

        fis.close();
        fos.close();

        /*FileInputStream*/ fis = new FileInputStream("C:\\Users\\adm-sabitovka\\Desktop\\2.txt");
        /*FileOutputStream*/ fos = new FileOutputStream("C:\\Users\\adm-sabitovka\\Desktop\\3.txt");

        Decoder decoder = new LZ78Decoder();
        decoder.decode(fis, fos);

        fis.close();
        fos.close();
    }
}
