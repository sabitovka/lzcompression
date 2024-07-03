package io.sabitovka.donntu.lzcompression.client.compression;

import io.sabitovka.donntu.lzcompression.client.compression.algorithms.lz77.LZ77Compressor;
import io.sabitovka.donntu.lzcompression.client.compression.algorithms.lz78.LZ78Compressor;
import io.sabitovka.donntu.lzcompression.client.compression.algorithms.lzss.LZSSCompressor;
import io.sabitovka.donntu.lzcompression.client.compression.algorithms.lzw.LZWCompressor;

public class CompressorFactory {

    public static Compressor getCompressor(String methodType) {
        Compressor compressor;
        if (methodType.toLowerCase().equals("lz77")) {
            return new LZ77Compressor();
        }
        if (methodType.toLowerCase().equals("lzss")) {
            return new LZSSCompressor();
        }
        if (methodType.toLowerCase().equals("lz78")) {
            return new LZ78Compressor();
        }
        if (methodType.toLowerCase().equals("lzw")) {
            return new LZWCompressor();
        }
        throw new IllegalStateException("Method type \"" + methodType + "\" not recognized");
    }

}
