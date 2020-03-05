package dev.houshce29.cc.lex;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TU_Context {

    private Context context;

    @Before
    public void beforeEach() {
        context = new Context();
    }

    @Test
    public void testIncrementLineNumber() {
        context.incrementLineNumber();
        Assert.assertEquals(2, context.getLineNumber());

        context.incrementLineNumber();
        Assert.assertEquals(3, context.getLineNumber());

        context.incrementLineNumber();
        Assert.assertEquals(4, context.getLineNumber());

        context.incrementLineNumber();
        Assert.assertEquals(5, context.getLineNumber());
    }

    @Test
    public void testIncrementLineNumberAndIgnore() {
        IgnoredToken token = context.incrementLineNumberAndIgnore();
        Assert.assertEquals(2, context.getLineNumber());
        Assert.assertNotNull(token);

        token = context.incrementLineNumberAndIgnore();
        Assert.assertEquals(3, context.getLineNumber());
        Assert.assertNotNull(token);

        token = context.incrementLineNumberAndIgnore();
        Assert.assertEquals(4, context.getLineNumber());
        Assert.assertNotNull(token);

        token = context.incrementLineNumberAndIgnore();
        Assert.assertEquals(5, context.getLineNumber());
        Assert.assertNotNull(token);
    }

    @Test
    public void testIncrementLineNumberAndReturn() {
        Token token = new SimpleToken("LINEBREAK", "\n", 1);
        Token returned = context.incrementLineNumberAndReturn(token);
        Assert.assertEquals(2, context.getLineNumber());
        Assert.assertEquals(token, returned);

        returned = context.incrementLineNumberAndReturn(token);
        Assert.assertEquals(3, context.getLineNumber());
        Assert.assertEquals(token, returned);

        returned = context.incrementLineNumberAndReturn(token);
        Assert.assertEquals(4, context.getLineNumber());
        Assert.assertEquals(token, returned);

        returned = context.incrementLineNumberAndReturn(token);
        Assert.assertEquals(5, context.getLineNumber());
        Assert.assertEquals(token, returned);
    }
}
