package io.github.sabkar.datacompression.compression;

import io.github.sabkar.datacompression.compression.algorithms.lz77.LZ77Compressor;
import io.github.sabkar.datacompression.compression.algorithms.lz78.LZ78Compressor;
import io.github.sabkar.datacompression.compression.algorithms.lzss.LZSSCompressor;
import io.github.sabkar.datacompression.compression.algorithms.lzw.LZWCompressor;

public class CompressorFactory {

    Compressor getCompressor(String methodType) {
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
