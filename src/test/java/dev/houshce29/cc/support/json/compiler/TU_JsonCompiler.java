package dev.houshce29.cc.support.json.compiler;

import dev.houshce29.cc.support.json.model.Json;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TU_JsonCompiler {
    private static final String JSON = "{" +
            "\"emptyObject\": {}," +
            "\"emptyArray\": []," +
            "\"emptyString\": \"\"," +
            "\"nullValue\": null," +
            "\"trueValue\": true," +
            "\"falseValue\": false," +
            "\"integer\": 20," +
            "\"float\": 3.14," +
            "\"string\": \"test-string\"," +
            "\"specialString\": \"escaped \\\"string\\\"\"," +
            "\"singletonArray\": [\"foo\"]," +
            "\"singletonObject\": {\"bar\": \"baz\"}," +
            "\"object\": {\"string\": \"str\"," +
            "             \"num\": 99," +
            "             \"bool\": true," +
            "             \"array\": [1,2,5]," +
            "             \"none\": null," +
            "             \"nested\": {\"hey\": \"hi\"}}," +
            "\"array\": [1, 9.09, true, false, \"str\", null," +
            "            {\"obj\": \"yes\"}, [1,2,8, \"four\"]]" +
            "}";

    private JsonCompiler compiler;

    @Before
    public void beforeEach() {
        compiler = new JsonCompiler();
    }

    @Test
    public void testCompile() {
        Json json = compiler.compile(JSON);
        Assert.assertFalse(json.path("doesNotExist").exists());
        Assert.assertTrue(json.path("emptyObject").isObjectValue());
        Assert.assertTrue(json.path("emptyArray").isArrayValue());
        Assert.assertTrue(json.path("emptyString").stringValue().isEmpty());
        Assert.assertTrue(json.path("nullValue").isNull());
        Assert.assertTrue(json.path("trueValue").booleanValue());
        Assert.assertFalse(json.path("falseValue").booleanValue());
        Assert.assertEquals(20, json.path("integer").numberValue().intValue());
        Assert.assertEquals(3.14, json.path("float").numberValue().doubleValue(), 0d);
        Assert.assertEquals("test-string", json.path("string").stringValue());
        Assert.assertEquals("escaped \"string\"", json.path("specialString").stringValue());
        Assert.assertEquals("foo", json.path("singletonArray").get(0).stringValue());
        Assert.assertEquals("baz", json.path("singletonObject").path("bar").stringValue());
        Assert.assertEquals("str", json.getAt("object", "string").stringValue());
        Assert.assertEquals(99, json.getAt("object", "num").numberValue().intValue());
        Assert.assertTrue(json.getAt("object", "bool").booleanValue());
        Assert.assertEquals(1, json.getAt("object", "array", 0).numberValue().intValue());
        Assert.assertEquals(2, json.getAt("object", "array", 1).numberValue().intValue());
        Assert.assertEquals(5, json.evaluate("object.array[2]").numberValue().intValue());
        Assert.assertTrue(json.getAt("object", "none").isNull());
        Assert.assertEquals("hi", json.evaluate("object.nested.hey").stringValue());
        Assert.assertEquals(1, json.path("array").path("0").numberValue().intValue());
        Assert.assertEquals(9.09, json.path("array").get(1).numberValue().doubleValue(), 0d);
        Assert.assertTrue(json.evaluate("array.2").booleanValue());
        Assert.assertFalse(json.path("array").get(3).booleanValue());
        Assert.assertEquals("str", json.path("array").get(4).stringValue());
        Assert.assertTrue(json.evaluate("array[5]").isNull());
        Assert.assertEquals("yes", json.evaluate("array[6].obj").stringValue());
        Assert.assertEquals(1, json.evaluate("array[7][0]").numberValue().intValue());
        Assert.assertEquals(2, json.evaluate("array.7.1").numberValue().intValue());
        Assert.assertEquals(8, json.evaluate("array[7].2").numberValue().intValue());
        Assert.assertEquals("four", json.evaluate("array.7[3]").stringValue());
    }
}