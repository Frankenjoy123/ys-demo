package com.yunsoo.auth;

import com.yunsoo.auth.api.security.authentication.TokenAuthenticationService;
import com.yunsoo.common.web.client.AsyncRestClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by:   Lijian
 * Created on:   2016-02-16
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebIntegrationTest("server.port=0")
@Ignore
public class TestBase {

    protected static AsyncRestClient restClient;
    protected static final String YUNSU_ORG_ID = "2k0r1l55i2rs5544wz5";

    @Value("${local.server.port}")
    public int port;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;


    @Before
    public void initRestClient() {
        if (restClient == null) {
            System.out.println("initializing restClient");
            String appId = "AuthUnitTest";
            String deviceId = getHostName();
            String accessToken = tokenAuthenticationService.generateAccessToken(Constants.SYSTEM_ACCOUNT_ID, YUNSU_ORG_ID).getToken();
            restClient = new AsyncRestClient("http://localhost:" + port);
            restClient.setPreRequestCallback(request -> {
                HttpHeaders httpHeaders = request.getHeaders();
                httpHeaders.set(Constants.HttpHeaderName.APP_ID, appId);
                httpHeaders.set(Constants.HttpHeaderName.DEVICE_ID, deviceId);
                httpHeaders.set(Constants.HttpHeaderName.ACCESS_TOKEN, accessToken);
            });
        }
    }

    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown";
        }
    }

}
