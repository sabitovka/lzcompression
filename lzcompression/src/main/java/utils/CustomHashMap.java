package utils;

import java.util.HashMap;
import java.util.Map;

public class CustomHashMap<K, V> extends HashMap<K, V> {

    public CustomHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public CustomHashMap() {
    }

    public CustomHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public CustomHashMap(Map<? extends K, ? extends V> m) {
        super(m);
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
