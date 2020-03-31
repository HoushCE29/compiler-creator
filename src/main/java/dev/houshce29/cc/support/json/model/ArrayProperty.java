package dev.houshce29.cc.support.json.model;

import java.util.List;

/**
 * Entry for arrays.
 */
public class ArrayProperty extends JsonProperty {
    private final List<JsonProperty> array;

    ArrayProperty(String key, List<JsonProperty> array) {
        super(key, array);
        this.array = array;
    }

    public boolean isEmpty() {
        return array.isEmpty();
    }

    @Override
    public boolean isArrayValue() {
        return true;
    }

    @Override
    public JsonProperty get(int index) {
        if (index >= array.size() || index < 0) {
            return new MissingProperty(String.valueOf(index));
        }
        return array.get(index);
    }

    @Override
    public JsonProperty path(String key) {
        try {
            return get(Integer.parseInt(key));
        }
        catch (Exception ex) {
            // swallow
        }
        return super.path(key);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append('"')
                .append(getKey())
                .append("\": [");

        for (int i = 0; i < array.size(); i++) {
            // Strip the other toString() results
            // "foo": "bar" => "bar"
            String propertyString = array.get(i).toString();
            int removePoint = propertyString.indexOf(':');
            String arrayValue = propertyString.substring(removePoint + 1);
            builder.append(arrayValue);
            if (i < array.size() - 1) {
                builder.append(',');
            }
        }
        return builder.append("]").toString();
    }
}
