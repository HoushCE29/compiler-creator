package dev.houshce29.cc.analyze;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TU_SymbolTable {
    private SymbolTable<TestData> table;

    @Before
    public void beforeEach() {
        table = new SymbolTable<>();
    }

    @Test
    public void test() {
        Assert.assertTrue(table.isEmpty());

        table.openScope();
        Assert.assertEquals(0, table.getCurrentScopeLevel());

        table.put("foo", new TestData("int"));
        table.put("bar", new TestData("object"));
        table.openScope();
        Assert.assertEquals(1, table.getCurrentScopeLevel());

        table.put("foo", new TestData("string"));

        Assert.assertEquals(1, table.findLatestScope("foo").get().getLevel());
        Assert.assertEquals("string", table.findLatest("foo").get().getVariableType());
        Assert.assertEquals(0, table.findLatestScope("bar").get().getLevel());
        Assert.assertEquals("object", table.findLatest("bar").get().getVariableType());

        table.closeScope();
        Assert.assertEquals(0, table.getCurrentScopeLevel());

        Assert.assertEquals(0, table.findLatestScope("foo").get().getLevel());
        Assert.assertEquals("int", table.findLatest("foo").get().getVariableType());
        Assert.assertEquals(0, table.findLatestScope("bar").get().getLevel());
        Assert.assertEquals("object", table.findLatest("bar").get().getVariableType());
    }

    private static final class TestData {
        private final String variableType;

        public TestData(String variableType) {
            this.variableType = variableType;
        }

        public String getVariableType() {
            return variableType;
        }
    }
}
