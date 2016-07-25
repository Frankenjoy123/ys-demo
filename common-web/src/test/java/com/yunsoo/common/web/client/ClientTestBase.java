package com.yunsoo.common.web.client;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by:   Lijian
 * Created on:   2016-07-22
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MockRestApiServer.class})
@WebIntegrationTest("server.port=0")
@Ignore
public class ClientTestBase {

    @Value("${local.server.port}")
    public int port;

}
