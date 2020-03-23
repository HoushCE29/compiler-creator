package dev.houshce29.cc.support.json.compiler;

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
                    .sentence("STRING_LITERAL")
                    .sentence("BOOLEAN_LITERAL")
                    .sentence("NUMBER_LITERAL")
                    // [$value . . .
                    .sentence("L_SQ_BRACKET", "VALUE", "ARRAY_END")
                .branch("ARRAY_END")
                    // . . . , $value . . .
                    .sentence("COMMA", "VALUE", "ARRAY_END")
                    // . . . ]
                    .sentence("R_SQ_BRACKET")
                .build();
    }
}
