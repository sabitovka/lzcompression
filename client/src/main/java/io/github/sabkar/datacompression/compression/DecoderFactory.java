package io.github.sabkar.datacompression.compression;

import io.github.sabkar.datacompression.compression.algorithms.lz77.LZ77Decoder;
import io.github.sabkar.datacompression.compression.algorithms.lz78.LZ78Decoder;
import io.github.sabkar.datacompression.compression.algorithms.lzss.LZSSDecoder;
import io.github.sabkar.datacompression.compression.algorithms.lzw.LZWDecoder;

public class DecoderFactory {

    Decoder getDecoder(String methodType) {
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
