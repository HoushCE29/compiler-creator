package dev.houshce29.cc.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class TU_Pair {

    @Test
    public void testPair() {
        String key = "KEY";
        Object value = new Object();

        Pair<String, Object> pair = Pair.of(key, value);

        Assert.assertEquals(key, pair.getKey());
        Assert.assertEquals(value, pair.getValue());
    }

    @Test
    public void testIdentityPair() {
        String self = "SELF";
        Pair<Object, Object> pair = Pair.identity(self);
        Assert.assertEquals(self, pair.getKey());
        Assert.assertEquals(self, pair.getValue());
    }
}
