package io.github.sabkar.lzcompression.client.compression;

import io.github.sabkar.lzcompression.client.compression.algorithms.lz77.LZ77Decoder;
import io.github.sabkar.lzcompression.client.compression.algorithms.lz78.LZ78Decoder;
import io.github.sabkar.lzcompression.client.compression.algorithms.lzss.LZSSDecoder;
import io.github.sabkar.lzcompression.client.compression.algorithms.lzw.LZWDecoder;

public class DecoderFactory {

    public static Decoder getDecoder(String methodType) {
        Compressor compressor;
        if (methodType.toLowerCase().equals("lz77")) {
            return new LZ77Decoder();
        }
        if (methodType.toLowerCase().equals("lzss")) {
            return new LZSSDecoder();
        }
        if (methodType.toLowerCase().equals("lz78")) {
            return new LZ78Decoder();
        }
        if (methodType.toLowerCase().equals("lzw")) {
            return new LZWDecoder();
        }
        throw new IllegalStateException("Method type \"" + methodType + "\" not recognized");
    }

}
