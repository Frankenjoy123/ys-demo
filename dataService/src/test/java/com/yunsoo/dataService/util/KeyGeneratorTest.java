package com.yunsoo.dataService.util;

import com.yunsoo.util.KeyGenerator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class KeyGeneratorTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public KeyGeneratorTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(KeyGeneratorTest.class);
    }

    public void test_KeyGenerator_newKey() {
        String key = KeyGenerator.newKey();
        //System.out.println(key);
        assertTrue(key != null && key.length() == 22);
    }
}