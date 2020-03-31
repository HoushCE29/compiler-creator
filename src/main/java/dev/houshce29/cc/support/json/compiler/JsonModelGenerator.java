package dev.houshce29.cc.support.json.compiler;

import dev.houshce29.cc.common.GrammarComponent;
import dev.houshce29.cc.generate.Generator;
import dev.houshce29.cc.lex.Token;
import dev.houshce29.cc.parse.SymbolTree;
import dev.houshce29.cc.parse.SymbolTreeNode;
import dev.houshce29.cc.support.json.model.ArrayProperty;
import dev.houshce29.cc.support.json.model.Json;
import dev.houshce29.cc.support.json.model.JsonFactory;
import dev.houshce29.cc.support.json.model.JsonProperty;
import dev.houshce29.cc.support.json.model.ObjectProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Generator from the json symbol tree to the java model.
 */
public class JsonModelGenerator implements Generator {

    @Override
    public Object generate(List<Token> tokens, SymbolTree tree) {
        return generate(tree);
    }

    public Json generate(SymbolTree tree) {
        SymbolTreeNode root = tree.getRoot();
        if (!JsonParserFactory.isJsonObject(root)) {
            throw new IllegalArgumentException("Expected root to be a JSON object.");
        }
        return JsonFactory.newJson(root.getChildren().stream()
                // Find the BODY element
                .filter(JsonParserFactory::isJsonObjectBody)
                // Cast to node
                .map(SymbolTreeNode::cast)
                // Map the body
                .map(this::mapBody)
                .findFirst()
                // If not found, no big deal -- means an empty JSON blob
                .orElse(Collections.emptyList()));
    }

    private Collection<JsonProperty> mapBody(SymbolTreeNode body) {
        List<JsonProperty> properties = new ArrayList<>();
        String currentKey = "";
        for (GrammarComponent component : body.getChildren()) {
            // String literal found, this is a key
            if (JsonLexerFactory.isStringLiteral(component)) {
                currentKey = Token.valueOf(component);
            }
            // Value found for the current key; can build the property.
            else if (JsonParserFactory.isJsonValue(component)) {
                properties.add(mapValue(currentKey, SymbolTreeNode.cast(component)));
            }
            // There is more body to expand; add all results here.
            else if (JsonParserFactory.isJsonObjectBody(component)) {
                properties.addAll(mapBody(SymbolTreeNode.cast(component)));
            }
        }

        return properties;
    }

    private JsonProperty mapValue(String key, SymbolTreeNode jsonValueNode) {
        // Grab the first component. This will determine where to go next.
        GrammarComponent definition = jsonValueNode.get(0);

        // If object, create object and return it.
        if (JsonParserFactory.isJsonObject(definition)) {
            return mapObject(key, SymbolTreeNode.cast(definition));
        }
        // If null, return null property
        else if (JsonLexerFactory.isNullLiteral(definition)) {
            return JsonFactory.newNull(key);
        }
        // If string literal, return sanitized property
        else if (JsonLexerFactory.isStringLiteral(definition)) {
            return JsonFactory.newString(key, Token.valueOf(definition));
        }
        // If boolean literal, parse it and return property
        else if (JsonLexerFactory.isBooleanLiteral(definition)) {
            return JsonFactory.newBoolean(key, Boolean.parseBoolean(Token.valueOf(definition)));
        }
        // If number value, concat as necessary and return property
        else if (JsonParserFactory.isJsonNumberValue(definition)) {
            return mapNumber(key, SymbolTreeNode.cast(definition));
        }

        // At this point, this is probably an array
        else if (JsonParserFactory.isArrayStart(definition)) {
            GrammarComponent next = jsonValueNode.get(1);
            // Check if empty first.
            if (JsonParserFactory.isArrayEnd(next)) {
                return JsonFactory.newArray(key, Collections.emptyList());
            }
            // This is a fully populated array.
            return mapArrayValue(key, SymbolTreeNode.cast(next), jsonValueNode.getNode(2));
        }

        throw new IllegalArgumentException(jsonValueNode.getId() + " did not resolve to a value.");
    }

    private JsonProperty mapNumber(String key, SymbolTreeNode jsonNumberNode) {
        String number = jsonNumberNode.getToken(0).getValue();
        if (jsonNumberNode.is("NUMBER_LITERAL", "DECIMAL_LITERAL")) {
            number += jsonNumberNode.getToken(1).getValue();
        }
        else if (jsonNumberNode.is("DECIMAL_LITERAL")) {
            number = "0" + number;
        }
        return JsonFactory.newNumber(key, JsonProperty.parseNumber(number));
    }

    private ObjectProperty mapObject(String key, SymbolTreeNode jsonObjectNode) {
        return JsonFactory.newObject(key, jsonObjectNode.getChildren().stream()
                // Find the BODY element
                .filter(JsonParserFactory::isJsonObjectBody)
                // Cast to node
                .map(SymbolTreeNode::cast)
                // Map the body
                .map(this::mapBody)
                .findFirst()
                // If not found, no big deal -- means an empty JSON blob
                .orElse(Collections.emptyList()));
    }

    private ArrayProperty mapArrayValue(String key, SymbolTreeNode initialJsonValueNode, SymbolTreeNode jsonArrayEndNode) {
        List<JsonProperty> elements = new ArrayList<>();
        elements.add(mapValue("0", initialJsonValueNode));
        elements.addAll(mapArrayToEnd(1, jsonArrayEndNode));
        return JsonFactory.newArray(key, elements);
    }

    private List<JsonProperty> mapArrayToEnd(int index, SymbolTreeNode jsonArrayEndNode) {
        List<JsonProperty> elements = new ArrayList<>();
        for (GrammarComponent component : jsonArrayEndNode.getChildren()) {
            // If value, map it out; set key to current index
            if (JsonParserFactory.isJsonValue(component)) {
                elements.add(mapValue(String.valueOf(index), SymbolTreeNode.cast(component)));
            }
            // Otherwise, there are more array elements or the end bracket
            else if (JsonParserFactory.isContinuedArray(component)) {
                elements.addAll(mapArrayToEnd(index++, SymbolTreeNode.cast(component)));
            }
        }
        return elements;
    }
}
