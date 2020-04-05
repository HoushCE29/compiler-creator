package dev.houshce29.cc.lex;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TU_Lexer {
    private static final String PROGRAM =
            "{Here, we set the time and farm activity}\n" +
            "the  time is    morning\n" +
            "the rooster  says      'COCKADOODLEDOO'\n";

    private Lexer lexer;

    @Before
    public void beforeEach() {
        lexer = Lexer.newBuilder()
                // Increment the line number on line breaks.
                .on(RegexFactory.lineSeparatorRegex())
                    .incrementLineNumber()

                // Ignore spaces, tabs, line breaks, etc.
                .ignore(RegexFactory.anyAmountWhitespaceRegex())

                // Ignore comments
                .on("\\{.*\\}")
                    .create(in -> new IgnoredToken("__COMMENT__", in.getCapturedValue(), in.getLineNumber()))

                .on("the|is")
                    .create(in -> new ArticleToken(in.getCapturedValue(), in.getLineNumber()))

                .on("morning|day|night")
                    .create(in -> new WhenToken(in.getCapturedValue(), in.getLineNumber()))

                .on("time")
                    .create(in -> new TimeToken(in.getLineNumber()))

                .on("rooster|cow|pig")
                    .create(in -> new AnimalToken(in.getCapturedValue(), in.getLineNumber()))

                .on("says|eats|produces|does")
                    .create(in -> new ActionToken(in.getCapturedValue(), in.getLineNumber()))

                .on("'.*'")
                    .create(in -> new StringLiteralToken(in.getCapturedValue().trim().replaceAll("'", ""),
                                                           in.getLineNumber()))

                .on("TORNADO")
                    .error(in -> new IncorrectException(in.getCapturedValue(), in.getLineNumber()))
                .build();
    }

    @Test
    public void testLexerBuildsTokensOnRegex() {
        List<? extends Token> tokens = lexer.lex(PROGRAM);
        Assert.assertEquals(8, tokens.size());
        validateToken(tokens.get(0), "ARTICLE", "the", 2);
        validateToken(tokens.get(1), "TIME", "time", 2);
        validateToken(tokens.get(2), "ARTICLE", "is", 2);
        validateToken(tokens.get(3), "WHEN", "morning", 2);
        validateToken(tokens.get(4), "ARTICLE", "the", 3);
        validateToken(tokens.get(5), "ANIMAL", "rooster", 3);
        validateToken(tokens.get(6), "ACTION", "says", 3);
        validateToken(tokens.get(7), "STRING_LITERAL", "COCKADOODLEDOO", 3);
    }

    @Test(expected = IncorrectException.class)
    public void testLexerThrowsExceptionOnRegex() {
        String badProgram = PROGRAM + " TORNADO";
        lexer.lex(badProgram);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLexerThrowsExceptionWhenNoRegexMatch() {
        String badProgram = PROGRAM + " whales";
        lexer.lex(badProgram);
    }

    @Test
    public void testLexerWithSpanStrategy() {
        Lexer spanLexer = Lexer.newBuilder()
                .on("a+", MatchingStrategy.SPAN)
                    .create(ctx -> new SimpleToken("A", ctx))
                .on("xa", MatchingStrategy.SPAN)
                    .create(ctx -> new SimpleToken("X_AND_A", ctx))
                .build();

        // Tokens should be aaa,xa,aaa instead of a,a,a,xa,a,a,a
        List<Token> tokens = spanLexer.lex("aaaxaaaa");
        Assert.assertEquals(3, tokens.size());
        Assert.assertEquals("aaa", tokens.get(0).getValue());
        Assert.assertEquals("xa", tokens.get(1).getValue());
        Assert.assertEquals("aaa", tokens.get(2).getValue());
    }

    @Test
    public void testLexerWithMaxStrategy() {
        Lexer maxLexer = Lexer.newBuilder()
                .on(".*a", MatchingStrategy.MAX)
                    .create(ctx -> new SimpleToken("MAX_A", ctx))
                .on("x", MatchingStrategy.MAX)
                    .create(ctx -> new SimpleToken("X", ctx))
                .build();

        // Tokens should be axaxxa and x
        List<Token> tokens = maxLexer.lex("axaxxax");
        Assert.assertEquals(2, tokens.size());
        Assert.assertEquals("axaxxa", tokens.get(0).getValue());
        Assert.assertEquals("x", tokens.get(1).getValue());
    }

    private void validateToken(Token token, String expectedId, String expectedValue, int expectedLineNumber) {
        Assert.assertEquals(expectedId, token.getId());
        Assert.assertEquals(expectedValue, token.getValue());
        Assert.assertEquals(expectedLineNumber, token.getLineNumber());
    }

    // ============ Custom tokens for the made-up farm scenario language

    private static final class ArticleToken extends SimpleToken {
        static final String ID = "ARTICLE";
        ArticleToken(String article, int lineNumber) {
            super(ID, article.trim(), lineNumber);
        }
    }

    private static final class TimeToken extends SimpleToken {
        static final String ID = "TIME";
        static final String VALUE = "time";
        TimeToken(int lineNumber) {
            super(ID, VALUE, lineNumber);
        }
    }

    private static final class WhenToken extends SimpleToken {
        static final String ID = "WHEN";
        WhenToken(String when, int lineNumber) {
            super(ID, when.trim(), lineNumber);
        }
    }

    private static final class AnimalToken extends SimpleToken {
        static final String ID = "ANIMAL";
        AnimalToken(String animal, int lineNumber) {
            super(ID, animal.trim(), lineNumber);
        }
    }

    private static final class ActionToken extends SimpleToken {
        static final String ID = "ACTION";
        ActionToken(String action, int lineNumber) {
            super(ID, action.trim(), lineNumber);
        }
    }

    private static final class StringLiteralToken extends SimpleToken {
        static final String ID = "STRING_LITERAL";
        StringLiteralToken(String string, int lineNumber) {
            super(ID, string.trim(), lineNumber);
        }
    }

    private static final class IncorrectException extends RuntimeException {
        private static final String MESSAGE = "A %s has been added on line %d.";
        IncorrectException(String literal, int line) {
            super(String.format(MESSAGE, literal, line));
        }
    }
}
