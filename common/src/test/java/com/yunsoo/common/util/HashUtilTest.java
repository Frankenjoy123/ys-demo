package com.yunsoo.common.util;

import org.junit.Test;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/21
 * Descriptions:
 */
public class HashUtilTest {

    @Test
    public void test_hash() {
        String src = "adminhacksalt";
        String result = HashUtil.hash(src);
        System.out.println(result);
    }
}
