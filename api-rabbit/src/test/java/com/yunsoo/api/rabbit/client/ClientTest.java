package com.yunsoo.api.rabbit.client;

import com.yunsoo.api.rabbit.config.ClientConfiguration;
import com.yunsoo.common.web.client.RestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ClientConfiguration.class})
@EnableAutoConfiguration
@WebAppConfiguration
public class ClientTest {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private RestClient processorClient;

    @Test
    public void testGetBaseURL() {
        System.out.println(dataAPIClient.getBaseUrl());
        System.out.println(processorClient.getBaseUrl());
    }


}
