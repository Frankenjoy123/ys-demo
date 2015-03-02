package com.yunsoo.api;

import com.yunsoo.api.config.DataAPIConfiguration;
import com.yunsoo.api.dataclient.DataAPIClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataAPIConfiguration.class})
@WebAppConfiguration
public class DataClientTest {

    @Autowired
    private DataAPIClient dataAPIClient;

    @Test
    public void testDataAPIClient(){
        System.out.println(dataAPIClient.getBaseURI());
    }

}
