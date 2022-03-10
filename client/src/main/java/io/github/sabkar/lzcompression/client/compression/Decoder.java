package io.github.sabkar.lzcompression.client.compression;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Decoder {
    void decode(InputStream inputStream, OutputStream outputStream) throws IOException;
}
