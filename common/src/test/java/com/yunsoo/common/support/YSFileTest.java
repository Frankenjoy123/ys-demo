package com.yunsoo.common.support;

import org.junit.Test;

/**
 * Created by:   Lijian
 * Created on:   2016-03-11
 * Descriptions:
 */
public class YSFileTest {

    @Test
    public void test_All() {
        YSFile pks = new YSFile("PKS");
        pks.putHeader("Test", "tttt");
        pks.putHeader("Abcd", "aaa");
        pks.putHeader("a-hello", "[xxx: fef]");
        pks.putHeader(null, null);
        pks.putHeader("test-null", null);
        pks.addComments(null);
        pks.addComments("this is comments");
        System.out.println(pks.getHeader("a-hello"));
        System.out.println(pks.toString());

        YSFile pks1 = YSFile.read(pks.toBytes());
        System.out.println(pks1.toString());
        System.out.println(pks1.getHeader("a-hello"));
    }
}
