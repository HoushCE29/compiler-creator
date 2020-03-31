package dev.houshce29.cc.support.json.model;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Entry for objects.
 */
public class ObjectProperty extends JsonProperty {
    private static final String OBJECT_FORMAT = "\"%s\": {%s}";
    private final Map<String, JsonProperty> entries;

    ObjectProperty(String key, Collection<JsonProperty> entries) {
        super(key, entries);
        this.entries = entries.stream().collect(Collectors.toMap(JsonProperty::getKey, Function.identity()));
    }

    public Collection<JsonProperty> getProperties() {
        return entries.values();
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public boolean isObjectValue() {
        return true;
    }

    @Override
    public JsonProperty path(String key) {
        return entries.getOrDefault(key, new MissingProperty(key));
    }

    @Override
    public JsonProperty get(int index) {
        return path(String.valueOf(index));
    }

    @Override
    public String toString() {
        return String.format(OBJECT_FORMAT, getKey(),
                entries.values().stream()
                        .map(Objects::toString)
                        .collect(Collectors.joining(", ")));
    }
}
