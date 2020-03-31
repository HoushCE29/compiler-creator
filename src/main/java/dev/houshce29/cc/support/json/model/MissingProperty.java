package dev.houshce29.cc.support.json.model;

/**
 * Java-safe placeholder for non-existent entries.
 */
public class MissingProperty extends JsonProperty {
    private static final String VALUE = "<MISSING>";

    MissingProperty(String key) {
        super(key, VALUE);
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public String toString() {
        return "{{ Missing node " + getKey() + " }}";
    }
}
