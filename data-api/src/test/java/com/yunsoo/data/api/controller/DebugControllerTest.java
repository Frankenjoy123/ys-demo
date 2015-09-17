package com.yunsoo.data.api.controller;

import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.client.RestResponseErrorHandler;
import com.yunsoo.data.api.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2015/9/17
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class DebugControllerTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ConfigurableApplicationContext env;

    private RestClient dataAPIClient;

    @Before
    public void before() {
        dataAPIClient = new RestClient("http://localhost:" + port, new RestResponseErrorHandler());
        EnvironmentTestUtils.addEnvironment(env, "yunsoo.environment=local");
    }

    @Test
    public void test_info() {
        Map<String, Object> info = dataAPIClient.get("debug", new ParameterizedTypeReference<Map<String, Object>>() {
        });
        System.out.println(info);

    }

}