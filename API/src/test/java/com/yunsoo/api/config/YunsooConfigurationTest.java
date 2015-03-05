package com.yunsoo.api.config;

import com.yunsoo.common.error.DebugConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by:   Lijian
 * Created on:   2015/3/5
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {YunsooConfiguration.class})
public class YunsooConfigurationTest {

    @Autowired()
    private DebugConfig debugConfig;

    @Autowired
    private String dataAPIBaseURL;

    @Test
    public void test_debugConfig(){
        System.out.println(debugConfig.isDebugEnabled());
    }

    @Test
    public void test_dataAPIBaseURL(){
        System.out.println(dataAPIBaseURL);
    }

}
