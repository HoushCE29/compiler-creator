package dev.houshce29.cc.parse;

import dev.houshce29.cc.common.GrammarComponent;
import dev.houshce29.cc.lex.SimpleToken;
import dev.houshce29.cc.lex.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TU_Parser {
    private static final PlusToken PLUS = new PlusToken();
    private static final Lp LP = new Lp();
    private static final Rp RP = new Rp();

    // 1 + (2 + 3)
    private static final List<Token> ADDER = Arrays.asList(
        new LiteralToken(1), PLUS, LP, new LiteralToken(2), PLUS, new LiteralToken(3), RP);

    private Parser parser;

    @Before
    public void beforeEach() {
        parser  = Parser.newBuilder("ADDER")
                    .sentence("ADD")
                .branch("ADD")
                    .sentence("EXPRESSION", "PLUS", "EXPRESSION")
                .branch("EXPRESSION")
                    .sentence("LITERAL")
                    .sentence("LP", "ADD", "RP")
                .build();
    }

    @Test
    public void testParse() {
        SymbolTree tree = parser.parse(ADDER);
        System.out.println(parser);
        System.out.println(tree);
        Assert.assertEquals(3, tree.findTokens("LITERAL").size());
        Assert.assertEquals(2, tree.findTokens("PLUS").size());
        Assert.assertEquals(1, tree.findTokens("LP").size());
        Assert.assertEquals(1, tree.findTokens("RP").size());
        Assert.assertEquals(2, tree.findNodes("ADD").size());
        Assert.assertEquals(4, tree.findNodes("EXPRESSION").size());

        List<GrammarComponent> result = tree.flatten();
        Assert.assertEquals(14, result.size());
        assertNode(result.get(0), "ADDER");
        assertNode(result.get(1), "ADD");
        assertNode(result.get(2), "EXPRESSION");
        assertToken(result.get(3), "LITERAL", "1");
        assertToken(result.get(4), "PLUS", "+");
        assertNode(result.get(5), "EXPRESSION");
        assertToken(result.get(6), "LP", "(");
        assertNode(result.get(7), "ADD");
        assertNode(result.get(8), "EXPRESSION");
        assertToken(result.get(9), "LITERAL", "2");
        assertToken(result.get(10), "PLUS", "+");
        assertNode(result.get(11), "EXPRESSION");
        assertToken(result.get(12), "LITERAL", "3");
        assertToken(result.get(13), "RP", ")");
    }

    @Test
    public void testParseSyntaxError() {
        List<Token> badSyntax = new ArrayList<>(ADDER);
        // Put in a token that causes bad syntax
        badSyntax.set(2, PLUS);
        try {
            parser.parse(badSyntax);
            Assert.fail("Failed to catch bad syntax.");
        }
        catch (IllegalArgumentException ex) {
            Assert.assertEquals("Syntax error near token '+' on line 0.", ex.getMessage());
        }
    }

    @Test
    public void testParseUnexpectedToken() {
        List<Token> unexpected = new ArrayList<>(ADDER);
        // Append extra value to otherwise valid syntax
        unexpected.add(PLUS);
        try {
            parser.parse(unexpected);
            Assert.fail("Failed to catch bad syntax.");
        }
        catch (IllegalArgumentException ex) {
            Assert.assertEquals("Unexpected token '+' on line 0.", ex.getMessage());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseAmbiguousGrammar() {
        Parser.newBuilder("AMBIGUOUS")
                .sentence("AMBIGUOUS")
                .build()
                .parse(ADDER);
    }

    private void assertToken(GrammarComponent actual, String expectedId, String expectedValue) {
        Assert.assertTrue(actual instanceof Token);
        Assert.assertEquals(expectedValue, ((Token) actual).getValue());
        assertId(actual, expectedId);
    }

    private void assertNode(GrammarComponent actual, String expectedId) {
        Assert.assertTrue(actual instanceof SymbolTreeNode);
        assertId(actual, expectedId);
    }

    private void assertId(GrammarComponent component, String expectedId) {
        Assert.assertEquals(expectedId, component.getId());
    }

    private static final class PlusToken extends SimpleToken {
        private PlusToken(int lineNumber) {
            super("PLUS", "+", lineNumber);
        }

        private PlusToken() {
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
