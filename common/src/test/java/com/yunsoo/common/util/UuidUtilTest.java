package com.yunsoo.common.util;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by:   Lijian
 * Created on:   2015/4/15
 * Descriptions:
 */
public class UuidUtilTest {

    @Test
    public void test_get() throws InterruptedException {
        String id = UuidUtil.get().toString();
        System.out.println(id);
        System.out.println(UuidUtil.get().toByteArray().length);
        assert id != null;
    }
}
