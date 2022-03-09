package io.sabkar.lzcompression.algorithms;

import static io.sabkar.lzcompression.utils.Constants.BUFFER_SIZE_BIT_COUNT;
import static io.sabkar.lzcompression.utils.Constants.WINDOW_SIZE_BIT_COUNT;

public abstract class BaseAlgorithm {

    protected int dictBitCount;
    protected int dictSize;
    protected int bufferBitCount;
    protected int bufferSize;

    public BaseAlgorithm(int dictBitCount, int bufferBitCount) {
        this.dictBitCount = dictBitCount;
        this.bufferBitCount = bufferBitCount;
        dictSize = (1 << dictBitCount) - 1;
        bufferSize = (1 << bufferBitCount) - 1;
    }

    public BaseAlgorithm() {
        this(WINDOW_SIZE_BIT_COUNT, BUFFER_SIZE_BIT_COUNT);
    }

    protected void appendToDict(StringBuilder dict, String s) {
        dict.append(s);
        int len;
        if ((len = dict.length()) > this.dictSize) {
            dict.delete(0, len - this.dictSize);
        }
    }

    protected char popChar(StringBuilder buff, int index) {
        char s = buff.charAt(index);
        buff.delete(0, index+1);
        return s;
    }

}
