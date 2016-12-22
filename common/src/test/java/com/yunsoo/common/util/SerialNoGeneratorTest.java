package com.yunsoo.common.util;

import org.junit.Test;

/**
 * Created by:   Lijian
 * Created on:   2016-12-15
 * Descriptions:
 */
public class SerialNoGeneratorTest {

    @Test
    public void test_getSerialNo() {
        SerialNoGenerator serialNoGenerator = new SerialNoGenerator("abc001[001-999]xxx");

        System.out.println("totalCount\t: " + serialNoGenerator.getTotalCount());
        System.out.println("totalLength\t: " + serialNoGenerator.getTotalLength());
        System.out.println("0\t: " + serialNoGenerator.getSerialNo(0));
        System.out.println("1\t: " + serialNoGenerator.getSerialNo(1));
        System.out.println("998\t: " + serialNoGenerator.getSerialNo(998));
        try {
            serialNoGenerator.getSerialNo(-1);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("-1 is out of range");
        }
        try {
            serialNoGenerator.getSerialNo(999);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("999 is out of range");
        }
    }

    @Test
    public void test_getSubSerialNoPattern() {
        SerialNoGenerator serialNoGenerator = new SerialNoGenerator("abc001[001-999]xxx");

        System.out.println("totalCount\t: " + serialNoGenerator.getTotalCount());
        System.out.println("totalLength\t: " + serialNoGenerator.getTotalLength());
        System.out.println("0, 15   \t: " + serialNoGenerator.getSubSerialNoPattern(0, 15));
        System.out.println("1, 200  \t: " + serialNoGenerator.getSubSerialNoPattern(1, 200));
        System.out.println("200, 200\t: " + serialNoGenerator.getSubSerialNoPattern(200, 200));
        System.out.println("998, 1  \t: " + serialNoGenerator.getSubSerialNoPattern(998, 1));
        try {
            serialNoGenerator.getSubSerialNoPattern(-1, 1);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("-1, 1 is out of range");
        }
        try {
            serialNoGenerator.getSubSerialNoPattern(999, 1);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("999, 1 is out of range");
        }
        try {
            serialNoGenerator.getSubSerialNoPattern(200, 900);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("200, 900 is out of range");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_pattern_invalid1() {
        new SerialNoGenerator("abc001[-999]");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_pattern_invalid2() {
        new SerialNoGenerator("abc001[12-999]");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_pattern_invalid3() {
        new SerialNoGenerator("abc001[12-11]xxx");
    }
}
