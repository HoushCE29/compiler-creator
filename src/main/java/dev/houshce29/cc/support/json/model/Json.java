package dev.houshce29.cc.support.json.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Top-level view of the json model.
 */
public class Json extends ObjectProperty {

    Json(Collection<JsonProperty> childProperties) {
        super("JSON", childProperties);
    }

    /**
     * Gets the property at the given path.
     * @param path Path of Strings and Numbers pointing at some property.
     * @return Json property at the path, else a MissingProperty if not found.
     */
    public JsonProperty getAt(Object... path) {
        JsonProperty current = this;
        for (Object segment : path) {
            // This will account for numerical path (e.g. arrays) as well
            current = current.path(segment.toString());
        }
        return current;
    }

    /**
     * Parses dotted-array notation and returns the json property
     * at said point.
     * <p>
     * The expression argument here is dotted and array. This means
     * that dots and array square brackets (of numerics) are used to
     * access data.
     * <p>
     * Given the following JSON:
     * <pre>
     *     {
     *         "foo": {
     *             "bar": [
     *                 0,
     *                 "string",
     *                 {
     *                     "baz": [
     *                         true,
     *                         {
     *                             theTargetValue: "target"
     *                         },
     *                         null
     *                     ]
     *                 }
     *             ]
     *         }
     *     }
     * </pre>
     * to get to the value "target", the expression would look like:
     * <pre>
     *     foo.bar[2].baz[1].theTargetValue
     * </pre>
     *
     * Alternatively, array notation can be bypassed as regular dotted-only
     * notation:
     * <pre>
     *     foo.bar.2.baz.1.theTargetValue
     * </pre>
     * @param expression Expression to evaluate.
     * @return Json property pointed at by the expression, or a missing property if not found.
     */
    public JsonProperty evaluate(String expression) {
        List<String> path = new ArrayList<>();
        for (String rawSegment : expression.trim().split("\\.")) {
            String seg = rawSegment.trim();
            if (seg.contains("[") && seg.contains("]") && seg.indexOf('[') < seg.lastIndexOf(']')) {
                Arrays.stream(seg.split("\\["))
                        .map(String::trim)
                        .map(str -> {
                            int splitPoint = str.lastIndexOf("]");
                            if (splitPoint > -1) {
                                return str.substring(0, splitPoint).trim();
                            }
                            return str;
                        })
                        .forEach(path::add);
            }
            else {
                path.add(seg);
            }
        }

        return getAt(path.toArray());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        for (JsonProperty prop : this.getProperties()) {
            builder.append(prop);
        }
        return builder.append('}').toString();
    }
}
