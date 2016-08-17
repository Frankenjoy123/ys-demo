package com.yunsoo.common.web.client;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by:   Lijian
 * Created on:   2016-07-22
 * Descriptions:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {MockRestApiServer.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
public class ClientTestBase {

    @LocalServerPort
    public int port;

}
