package dev.houshce29.cc.support.json.compiler;

import dev.houshce29.cc.common.GrammarComponent;
import dev.houshce29.cc.parse.Parser;

/**
 * Holder for JSON parser.
 */
public final class JsonParserFactory {
    private JsonParserFactory() {
    }

    /**
     * @return New JSON parser instance.
     */
    public static Parser get() {
        return Parser.newBuilder("JSON_OBJECT")
                    // { BODY }
                    .sentence("L_BRACE", "BODY", "R_BRACE")
                    // {}
                    .sentence("L_BRACE", "R_BRACE")
                .branch("BODY")
                    // "key": $value, . . .
                    .sentence("STRING_LITERAL", "COLON", "VALUE", "COMMA", "BODY")
                    // "key": $value
                    .sentence("STRING_LITERAL", "COLON", "VALUE")
                .branch("VALUE")
                    // { . . . }
                    .sentence("JSON_OBJECT")
                    .sentence("NULL_LITERAL")
                    .sentence("STRING_LITERAL")
                    .sentence("BOOLEAN_LITERAL")
                    .sentence("NUMBER_VALUE")
                    // [$value . . .
                    .sentence("L_SQ_BRACKET", "VALUE", "ARRAY_END")
                    // []
                    .sentence("L_SQ_BRACKET", "R_SQ_BRACKET")
                .branch("NUMBER_VALUE")
                    // #+.#+
                    .sentence("NUMBER_LITERAL", "DECIMAL_LITERAL")
                    // #+
                    .sentence("NUMBER_LITERAL")
                    // .#+
                    .sentence("DECIMAL_LITERAL")
                .branch("ARRAY_END")
                    // . . . , $value . . .
                    .sentence("COMMA", "VALUE", "ARRAY_END")
                    // . . . ]
                    .sentence("R_SQ_BRACKET")
                .build();
    }

    public static boolean isJsonObject(GrammarComponent component) {
        return "JSON_OBJECT".equals(component.getId());
    }

    public static boolean isJsonValue(GrammarComponent component) {
        return "VALUE".equals(component.getId());
    }

    public static boolean isJsonNumberValue(GrammarComponent component) {
        return "NUMBER_VALUE".equals(component.getId());
    }

    public static boolean isJsonObjectBody(GrammarComponent component) {
        return "BODY".equals(component.getId());
    }

    public static boolean isContinuedArray(GrammarComponent component) {
        return "ARRAY_END".equals(component.getId());
    }

    public static boolean isArrayStart(GrammarComponent component) {
        return "L_SQ_BRACKET".equals(component.getId());
    }

    public static boolean isArrayEnd(GrammarComponent component) {
        return "R_SQ_BRACKET".equals(component.getId());
    }
}
