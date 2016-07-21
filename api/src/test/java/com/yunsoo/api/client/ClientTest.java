package com.yunsoo.api.client;

import com.yunsoo.api.config.ClientConfiguration;
import com.yunsoo.common.web.client.RestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ClientConfiguration.class})
public class ClientTest {

    @Autowired
    private AuthApiClient authApiClient;

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private RestClient processorClient;


    @Test
    public void testGetBaseURL() {
        System.out.println(dataAPIClient.getBaseUrl());
        System.out.println(processorClient.getBaseUrl());
    }

    @Test
    public void testAuthApiClient() {
        System.out.println("authApiClient: " + authApiClient.checkHealth().getStatus());
    }


}
