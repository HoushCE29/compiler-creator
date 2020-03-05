package dev.houshce29.cc.common.utils;

import java.util.Objects;

/**
 * Util for storing pairs.
 * Although tons of similar implementations exist,
 * this also ensures that consumers of this repo do
 * not have to worry about 3rd party lib/dependency issues.
 * @param <K> Key type.
 * @param <V> Value type.
 */
public class Pair<K, V> {
    private final K key;
    private final V value;

    /**
     * Creates a new pair.
     * @param key Key.
     * @param value Value.
     */
    private Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return Pair key.
     */
    public K getKey() {
        return key;
    }

    /**
     * @return Pair value.
     */
    public V getValue() {
        return value;
    }

    /**
     * Creates a new pair.
     * @param key Key of the pair.
     * @param value Value of the pair.
     * @param <K> Type of the key.
     * @param <V> Type of the value.
     * @return New pair.
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    /**
     * Creates a new identity pair (e.g. self => self).
     * @param self Key and value of the pair.
     * @param <T> Type of the self element.
     * @return New identity pair.
     */
    public static <T> Pair<T, T> identity(T self) {
        return new Pair<>(self, self);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair other = (Pair) obj;
        return Objects.equals(other.getKey(), key)
                && Objects.equals(other.getValue(), value);
    }

    @Override
    public String toString() {
        return "{" + key + ", " + value + "}";
    }
}
