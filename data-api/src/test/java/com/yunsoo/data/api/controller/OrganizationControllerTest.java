package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.OrganizationObject;
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
 * Created on:   2015/9/22
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class OrganizationControllerTest {

    @Value("${local.server.port}")
    private int port;

    private RestClient dataAPIClient;

    @Before
    public void before() {
        dataAPIClient = new RestClient("http://localhost:" + port, new RestResponseErrorHandler());
    }

    @Test
    public void test_YunsuOrg() {
        OrganizationObject organizationObject = dataAPIClient.get("organization/{id}", OrganizationObject.class, Constants.Ids.YUNSU_ORG_ID);
        assert organizationObject.getId().equals(Constants.Ids.YUNSU_ORG_ID);

    }
}
