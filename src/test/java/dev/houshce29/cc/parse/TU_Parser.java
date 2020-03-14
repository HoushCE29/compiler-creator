package dev.houshce29.cc.parse;

import dev.houshce29.cc.lex.SimpleToken;
import dev.houshce29.cc.lex.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TU_Parser {
    private static final PlusToken PLUS = new PlusToken();
    private static final MinusToken MINUS = new MinusToken();
    private static final Lp LP = new Lp();
    private static final Rp RP = new Rp();

    // 1 + (2 + 3)
    private static final List<Token> ADDER = Arrays.asList(
        new LiteralToken(1), PLUS, LP, new LiteralToken(2), PLUS, new LiteralToken(3), RP);

    private Parser parser;

    @Before
    public void beforeEach() {
        parser = Parser.from(Grammar.from(Phrase.newBuilder("SIMPLE_MATH")
                        .addSentence("ADD")
                        .addSentence("SUBTRACT")
                        .build())
                .addPhrase(Phrase.newBuilder("ADD")
                        .addSentence("EXPRESSION", "PLUS", "EXPRESSION")
                        .build())
                .addPhrase(Phrase.newBuilder("SUBTRACT")
                        .addSentence("EXPRESSION", "MINUS", "EXPRESSION")
                        .build())
                .addPhrase(Phrase.newBuilder("EXPRESSION")
                        .addSentence("LITERAL")
                        .addSentence("LP", "ADD", "RP")
                        .addSentence("LP", "SUBTRACT", "RP")
                        .build())
                .build());
    }

    @Test
    public void test() {
        System.out.println(parser.parse(ADDER));
    }

    private static final class PlusToken extends SimpleToken {
        private PlusToken(int lineNumber) {
            super("PLUS", "+", lineNumber);
        }

        private PlusToken() {
            this(0);
        }
    }

    private static final class MinusToken extends SimpleToken {
        private MinusToken(int lineNumber) {
            super("MINUS", "-", lineNumber);
        }

        private MinusToken() {
            this(0);
        }
    }

    private static final class Lp extends SimpleToken {
        private Lp() {
            super("LP", "(", 0);
        }
    }

    private static final class Rp extends SimpleToken {
        private Rp() {
            super("RP", ")", 0);
        }
    }

    private static final class LiteralToken extends SimpleToken {
        private LiteralToken(Object literal, int lineNumber) {
            super("LITERAL", literal.toString(), lineNumber);
        }

        private LiteralToken(Object literal) {
            this(literal, 0);
        }
    }
}
