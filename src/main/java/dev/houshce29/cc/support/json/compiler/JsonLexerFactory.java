package dev.houshce29.cc.support.json.compiler;

import dev.houshce29.cc.common.GrammarComponent;
import dev.houshce29.cc.lex.Lexer;
import dev.houshce29.cc.lex.MatchingStrategy;
import dev.houshce29.cc.lex.RegexFactory;
import dev.houshce29.cc.lex.ScanContext;
import dev.houshce29.cc.lex.SimpleToken;

/**
 * Holder for JSON lexer.
 */
public final class JsonLexerFactory {

    private JsonLexerFactory() {
    }

    /**
     * @return New JSON lexer instance.
     */
    public static Lexer get() {
        return Lexer.newBuilder()
                // Increment line number and ignore
                .on(RegexFactory.lineSeparatorRegex())
                    .create(ScanContext::incrementLineNumberAndIgnore)
                // Ignore misc. spaces
                .on(RegexFactory.anyAmountWhitespaceRegex())
                    .create(ScanContext::ignore)
                .on(RegexFactory.stringLiteralRegex())
                    .create(in -> {
                        String value = in.getCapturedValue();
                        value = value.substring(1, value.length() - 1);
                        if (value.contains("\"")) {
                            value = value.replaceAll("\\\\\"", "\"");
                        }
                        return new SimpleToken("STRING_LITERAL", value, in.getLineNumber());
                    })
                .on(RegexFactory.booleanLiteralRegex())
                    .create(in -> new SimpleToken("BOOLEAN_LITERAL", in))
                .on("null")
                    .create(in -> new SimpleToken("NULL_LITERAL", in))
                .on("[0-9]+", MatchingStrategy.SPAN)
                    .create(in -> new SimpleToken("NUMBER_LITERAL", in))
                .on("\\.[0-9]+", MatchingStrategy.SPAN)
                    .create(in -> new SimpleToken("DECIMAL_LITERAL", in))
                .on("\\{")
                    .create(in -> new SimpleToken("L_BRACE", "{", in.getLineNumber()))
                .on("\\}")
                    .create(in -> new SimpleToken("R_BRACE", "}", in.getLineNumber()))
                .on("\\[")
                    .create(in -> new SimpleToken("L_SQ_BRACKET", "[", in.getLineNumber()))
                .on("\\]")
                    .create(in -> new SimpleToken("R_SQ_BRACKET", "]", in.getLineNumber()))
                .on(":")
                    .create(in -> new SimpleToken("COLON", ":", in.getLineNumber()))
                .on(",")
                    .create(in -> new SimpleToken("COMMA", ",", in.getLineNumber()))
                .build();
    }

    public static boolean isStringLiteral(GrammarComponent component) {
        return "STRING_LITERAL".equals(component.getId());
    }

    public static boolean isBooleanLiteral(GrammarComponent component) {
        return "BOOLEAN_LITERAL".equals(component.getId());
    }

    public static boolean isNullLiteral(GrammarComponent component) {
        return "NULL_LITERAL".equals(component.getId());
    }

    public static boolean isNumberLiteral(GrammarComponent component) {
        return "NUMBER_LITERAL".equals(component.getId());
    }

    public static boolean isDecimalLiteral(GrammarComponent component) {
        return "DECIMAL_LITERAL".equals(component.getId());
    }
}
