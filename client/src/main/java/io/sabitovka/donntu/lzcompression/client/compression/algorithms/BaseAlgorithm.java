package io.sabitovka.donntu.lzcompression.client.compression.algorithms;

import io.sabitovka.donntu.lzcompression.client.compression.utils.Constants;

public abstract class BaseAlgorithm {

    protected final int dictBitCount;
    protected final int dictSize;
    protected final int bufferBitCount;
    protected final int bufferSize;

    protected BaseAlgorithm(int dictBitCount, int bufferBitCount) {
        this.dictBitCount = dictBitCount;
        this.bufferBitCount = bufferBitCount;
        dictSize = (1 << dictBitCount) - 1;
        bufferSize = (1 << bufferBitCount) - 1;
    }

    protected BaseAlgorithm() {
        this(Constants.WINDOW_SIZE_BIT_COUNT, Constants.BUFFER_SIZE_BIT_COUNT);
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
