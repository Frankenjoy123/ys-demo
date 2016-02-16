package com.yunsoo.data.api;

import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.client.RestResponseErrorHandler;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by:   Lijian
 * Created on:   2016-02-16
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest()
public abstract class ControllerTestBase {

    @Value("${local.server.port}")
    public int port;

    public static RestClient dataAPIClient;

    @Before
    public void initDataAPIClient() {
        if (dataAPIClient == null) {
            System.out.println("initializing data api client");
            dataAPIClient = new RestClient("http://localhost:" + port, new RestResponseErrorHandler());
        }
    }

}
