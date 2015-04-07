package com.yunsoo.api.config;

import com.yunsoo.common.config.CommonConfig;
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
    private CommonConfig commonConfig;

    @Autowired
    private YunsooYamlConfig yunsooYamlConfig;

    @Test
    public void test_debugConfig(){

        System.out.println(commonConfig.isDebugEnabled());
    }


}
