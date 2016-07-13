package com.yunsoo.auth;

import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.client.RestResponseErrorHandler;
import org.junit.Before;
import org.junit.Ignore;
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
@SpringApplicationConfiguration(classes = {Application.class})
@WebIntegrationTest()
@Ignore
public class TestBase {

    @Value("${local.server.port}")
    public int port;

    public static RestClient restClient;

    @Before
    public void initDataAPIClient() {
        if (restClient == null) {
            System.out.println("initializing data api client");
            restClient = new RestClient("http://localhost:" + port, new RestResponseErrorHandler());
        }
    }

}
