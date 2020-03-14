package dev.houshce29.cc.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * An entity that observes.
 */
public class Probe {
    private final Map<String, Object> probeData = new HashMap<>();

    /**
     * Observes information.
     * @param key Key of the info.
     * @param data Data.
     */
    public void observe(String key, Object data) {
        probeData.put(key, data);
    }

    /**
     * Resets the data of the given key.
     * @param key Key of the data.
     */
    public void reset(String key) {
        probeData.remove(key);
    }

    /**
     * Returns the data of the given key.
     * @param key Key of the data.
     * @return Object represented by the key.
     */
    public Object get(String key) {
        return probeData.get(key);
    }

    /**
     * Returns the data in the form of the given type.
     * @param key Key of the data.
     * @param asType Target type.
     * @param <T> Target type.
     * @return Data of the given type.
     */
    public <T> T get(String key, Class<T> asType) {
        Object raw = get(key);
        if (asType.isInstance(raw)) {
            return asType.cast(raw);
        }
        return null;
    }
}
