package io.github.sabkar.lzcompression.client.compression.utils;

public final class Constants {

    private Constants() {}

    public static final int WINDOW_SIZE_BIT_COUNT = 5;
    public static final int BUFFER_SIZE_BIT_COUNT = 3;
    public static final int WINDOW_SIZE = (1 << WINDOW_SIZE_BIT_COUNT) - 1;
    public static final int BUFFER_SIZE = (1 << BUFFER_SIZE_BIT_COUNT) - 1;

    public static final int MAX_TABLE_BIT_LENGTH = 9;
    public static final int MAX_TABLE_LENGTH = 1 << MAX_TABLE_BIT_LENGTH;
    public static final int TABLE_OFFSET = 256;

}
