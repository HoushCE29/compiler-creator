package dev.houshce29.cc.support.json.model;

import java.util.Collection;
import java.util.List;

public class JsonFactory {
    private JsonFactory() {
    }

    public static Json newJson(Collection<JsonProperty> childProperties) {
        return new Json(childProperties);
    }

    public static ObjectProperty newObject(String key, Collection<JsonProperty> childProperties) {
        return new ObjectProperty(key, childProperties);
    }

    public static ArrayProperty newArray(String key, List<JsonProperty> array) {
        return new ArrayProperty(key, array);
    }

    public static JsonProperty newNumber(String key, Number value) {
        return new JsonProperty(key, value);
    }

    public static JsonProperty newString(String key, String value) {
        return new JsonProperty(key, value);
    }

    public static JsonProperty newBoolean(String key, boolean value) {
        return new JsonProperty(key, value);
    }

    public static NullProperty newNull(String key) {
        return new NullProperty(key);
    }

    public static MissingProperty newMissing(String key) {
        return new MissingProperty(key);
    }
}
