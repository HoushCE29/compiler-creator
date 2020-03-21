package dev.houshce29.cc.lex;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TU_ScanContext {

    private ScanContext scanContext;

    @Before
    public void beforeEach() {
        scanContext = new ScanContext();
    }

    @Test
    public void testIncrementLineNumber() {
        scanContext.incrementLineNumber();
        Assert.assertEquals(2, scanContext.getLineNumber());

        scanContext.incrementLineNumber();
        Assert.assertEquals(3, scanContext.getLineNumber());

        scanContext.incrementLineNumber();
        Assert.assertEquals(4, scanContext.getLineNumber());

        scanContext.incrementLineNumber();
        Assert.assertEquals(5, scanContext.getLineNumber());
    }

    @Test
    public void testIncrementLineNumberAndIgnore() {
        IgnoredToken token = scanContext.incrementLineNumberAndIgnore();
        Assert.assertEquals(2, scanContext.getLineNumber());
        Assert.assertNotNull(token);

        token = scanContext.incrementLineNumberAndIgnore();
        Assert.assertEquals(3, scanContext.getLineNumber());
        Assert.assertNotNull(token);

        token = scanContext.incrementLineNumberAndIgnore();
        Assert.assertEquals(4, scanContext.getLineNumber());
        Assert.assertNotNull(token);

        token = scanContext.incrementLineNumberAndIgnore();
        Assert.assertEquals(5, scanContext.getLineNumber());
        Assert.assertNotNull(token);
    }

    @Test
    public void testIncrementLineNumberAndReturn() {
        Token token = new SimpleToken("LINEBREAK", "\n", 1);
        Token returned = scanContext.incrementLineNumberAndReturn(token);
        Assert.assertEquals(2, scanContext.getLineNumber());
        Assert.assertEquals(token, returned);

        returned = scanContext.incrementLineNumberAndReturn(token);
        Assert.assertEquals(3, scanContext.getLineNumber());
        Assert.assertEquals(token, returned);

        returned = scanContext.incrementLineNumberAndReturn(token);
        Assert.assertEquals(4, scanContext.getLineNumber());
        Assert.assertEquals(token, returned);

        returned = scanContext.incrementLineNumberAndReturn(token);
        Assert.assertEquals(5, scanContext.getLineNumber());
        Assert.assertEquals(token, returned);
    }
}
