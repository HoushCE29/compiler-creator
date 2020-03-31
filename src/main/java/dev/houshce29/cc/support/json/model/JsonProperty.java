package dev.houshce29.cc.support.json.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Basic JSON entry.
 */
public class JsonProperty {
    static final String DEFAULT_FORMAT = "\"%s\": %s";
    private static final String STRING_VALUE_FORMAT = "\"%s\": \"%s\"";
    private final String key;
    private final Object value;

    JsonProperty(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return JSON key.
     */
    public String getKey() {
        return key;
    }

    /**
     * @return JSON value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return JSON value as a string.
     */
    public String stringValue() {
        return value.toString();
    }

    /**
     * @return JSON value as a number.
     */
    public Number numberValue() {
        if (value instanceof Number) {
            return (Number) value;
        }
        return parseNumber(value.toString());
    }

    /**
     * @return JSON value as a boolean.
     */
    public boolean booleanValue() {
        if (isNull()) {
            return false;
        }
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        if (isArrayValue() && arrayValue().isEmpty()) {
            return false;
        }
        String stringValue = stringValue();
        return Boolean.parseBoolean(stringValue)
                || (stringValue.length() > 0 && !"0".equals(stringValue))
                || numberValue().intValue() > 0;
    }

    /**
     * @return JSON value as an array.
     */
    @SuppressWarnings("unchecked")
    public List<JsonProperty> arrayValue() {
        if (isArrayValue() && value instanceof List) {
            return (List<JsonProperty>) value;
        }
        return Collections.emptyList();
    }

    /**
     * @return JSON value as an object.
     */
    @SuppressWarnings("unchecked")
    public Map<String, JsonProperty> objectValue() {
        if (isObjectValue() && value instanceof Map) {
            return (Map<String, JsonProperty>) value;
        }
        return Collections.emptyMap();
    }

    /**
     * @return `true` if this entry has array as value.
     */
    public boolean isArrayValue() {
        return false;
    }

    /**
     * @return `true` if this entry has object as value.
     */
    public boolean isObjectValue() {
        return false;
    }

    /**
     * @return `true` if this entry has a null value.
     */
    public boolean isNull() {
        return false;
    }

    /**
     * @return `true` if this entry exists.
     */
    public boolean exists() {
        return true;
    }

    /**
     * Gets the child entry of the given key.
     * If the child doesn't exist or this entry is NOT an
     * object, then the missing entry is returned.
     * @param key Key of the child entry.
     * @return JsonProperty at the path.
     */
    public JsonProperty path(String key) {
        return new MissingProperty(key);
    }

    /**
     * Gets the entry at the given index if this
     * entry's value is an array. If this entry
     * is NOT an array, then the missing entry is
     * returned instead.
     * @param index Index in the array to get value of.
     * @return The value at the given index, or missing entry
     *         if out of bounds or this entry's value isn't a list.
     */
    public JsonProperty get(int index) {
        return new MissingProperty(String.valueOf(index));
    }

    /**
     * Parses a number for JSON uses.
     * @param input Input to parse.
     * @return The parsed number; if no parse could occur,
     *         0 is returned instead.
     */
    public static Number parseNumber(String input) {
        try {
            return Long.parseLong(input);
        }
        catch (Exception ex) {
            // Swallow, try different conversion
        }
        try {
            return Double.parseDouble(input);
        }
        catch (Exception ex) {
            return 0;
        }
    }

    @Override
    public String toString() {
        String format = DEFAULT_FORMAT;
        if (value instanceof String) {
            format = STRING_VALUE_FORMAT;
        }
        return String.format(format, key, value);
    }
}
