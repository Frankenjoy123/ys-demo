package com.yunsoo.dataService;

import com.yunsoo.service.contract.ProductCategory;
import com.yunsoo.service.ProductCategoryService;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Unit test for simple App.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@Configuration
//@EnableAutoConfiguration
//@ContextConfiguration(locations={"classpath:**/applicationContext.xml"})
public class AppTest
        extends TestCase {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

}

