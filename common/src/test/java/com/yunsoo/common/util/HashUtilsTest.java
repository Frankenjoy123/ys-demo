package com.yunsoo.common.util;

import org.junit.Test;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/21
 * Descriptions:
 */
public class HashUtilsTest {

    @Test
    public void test_hash() {
        String salt = RandomUtils.generateString(8);
        String src = "admin" + salt;
        System.out.println(salt);
        System.out.println(HashUtils.md5(src));
        System.out.println(HashUtils.sha1(src));
        System.out.println(HashUtils.sha256(src));

    }
}
