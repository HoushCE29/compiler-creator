package dev.houshce29.cc.support.json.compiler;

import dev.houshce29.cc.lex.Lexer;
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
                    .create(in -> new SimpleToken("STRING_LITERAL", in))
                .on(RegexFactory.booleanLiteralRegex())
                    .create(in -> new SimpleToken("BOOLEAN_LITERAL", in))
                .on(RegexFactory.numberLiteralRegex())
                    .create(in -> new SimpleToken("NUMBER_LITERAL", in))
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
}
