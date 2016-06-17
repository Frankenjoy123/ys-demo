package com.yunsoo.common.util;

import org.junit.Test;

/**
 * Created by:   Lijian
 * Created on:   2016-06-02
 * Descriptions:
 */
public class FileNameUtilsTest {

    @Test
    public void test_shorten() {
        String fileName1 = "1234567890.txt";
        System.out.println(fileName1 + " -> " + FileNameUtils.shorten(fileName1, 9));
        System.out.println(fileName1 + " -> " + FileNameUtils.shorten(fileName1, 14));
        System.out.println(fileName1 + " -> " + FileNameUtils.shorten(fileName1, 3));
        System.out.println(fileName1 + " -> " + FileNameUtils.shorten(fileName1, 4));
        System.out.println(fileName1 + " -> " + FileNameUtils.shorten(fileName1, 5));

        String fileName2 = ".txt-abcde";
        System.out.println(fileName2 + " -> " + FileNameUtils.shorten(fileName2, 6));
        System.out.println(fileName2 + " -> " + FileNameUtils.shorten(fileName2, 10));

        String fileName3 = "abcde.fghi.txt";
        System.out.println(fileName3 + " -> " + FileNameUtils.shorten(fileName3, 11));
        System.out.println(fileName3 + " -> " + FileNameUtils.shorten(fileName3, 6));

        String fileName4 = "1234567890";
        System.out.println(fileName4 + " -> " + FileNameUtils.shorten(fileName4, 5));

        String fileName5 = "1.txt";
        System.out.println(fileName5 + " -> " + FileNameUtils.shorten(fileName5, 4));

    }
}
