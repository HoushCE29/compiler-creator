package dev.houshce29.cc.support.json.model;

/**
 * Entry for null values.
 */
public class NullProperty extends JsonProperty {
    public static final String VALUE = "null";

    NullProperty(String key) {
        super(key, VALUE);
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public String toString() {
        return String.format(DEFAULT_FORMAT, getKey(), VALUE);
    }
}
