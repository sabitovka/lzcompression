package io.sabkar.lzcompression.utils;

import java.util.HashMap;
import java.util.Map;

public class CustomHashMap<K, V> extends HashMap<K, V> {

    public CustomHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public K getKeyByValue(V value) {
        for (Map.Entry<K, V> entry : this.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
