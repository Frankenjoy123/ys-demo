package com.yunsoo.key.service.util;

import org.junit.Test;

import java.util.stream.IntStream;

/**
 * Created by:   Lijian
 * Created on:   2016-08-23
 * Descriptions:
 */
public class BatchNoGeneratorTest {

    @Test
    public void test_getNew() {
        System.out.println(BatchNoGenerator.getNew());
    }

    @Test
    public void test_getNew_100() {
        IntStream.range(0, 100).forEach(i -> System.out.println(BatchNoGenerator.getNew()));
    }
}
