package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.client.RestResponseErrorHandler;
import com.yunsoo.data.api.Application;
import com.yunsoo.data.api.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by:   Lijian
 * Created on:   2015/9/23
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class UserControllerTest {

    @Value("${local.server.port}")
    private int port;

    private RestClient dataAPIClient;

    @Before
    public void before() {
        dataAPIClient = new RestClient("http://localhost:" + port, new RestResponseErrorHandler());
    }

    @Test
    public void test_AnonymousUser() {
        UserObject userObject = dataAPIClient.get("user/{id}", UserObject.class, Constants.Ids.ANONYMOUS_USER_ID);
        assert userObject.getId().equals(Constants.Ids.ANONYMOUS_USER_ID);

    }
}
