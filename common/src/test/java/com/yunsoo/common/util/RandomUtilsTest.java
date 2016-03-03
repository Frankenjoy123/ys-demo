package com.yunsoo.common.util;

import org.junit.Test;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/22
 * Descriptions:
 */
public class RandomUtilsTest {

    @Test
    public void test_generateString() {
        for (int i = 0; i < 33; i++) {
            System.out.println(RandomUtils.generateString(i));
        }
    }
}
