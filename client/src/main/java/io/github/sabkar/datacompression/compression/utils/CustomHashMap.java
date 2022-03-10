package io.github.sabkar.datacompression.compression.utils;

import java.util.HashMap;

public class CustomHashMap<K, V> extends HashMap<K, V> {

    public CustomHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public K getKeyByValue(V value) {
        for (Entry<K, V> entry : this.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
