package com.yunsoo.processor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by:   Lijian
 * Created on:   2015/4/3
 * Descriptions:
 */
public class YmlTest {

    @Value("${yunsoo.debug}")
    private String debug;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Test
    public void test_Yml() {
        System.out.println(debug);
        System.out.println(region);
    }
}
