package com.yunsoo.common.util;

import org.junit.Test;

/**
 * Created by:   Lijian
 * Created on:   2015/4/15
 * Descriptions:
 */
public class KeyGeneratorTest {

    @Test
    public void test_get() {
        String key = KeyGenerator.get();
        System.out.println(key);
        assert key != null && key.length() == 22;
    }
}