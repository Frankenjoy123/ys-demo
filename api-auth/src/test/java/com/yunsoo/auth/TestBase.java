package com.yunsoo.auth;

import com.yunsoo.auth.api.security.authentication.TokenAuthenticationService;
import com.yunsoo.common.web.client.AsyncRestClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by:   Lijian
 * Created on:   2016-02-16
 * Descriptions:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
public class TestBase {

    protected static AsyncRestClient restClient;
    protected static final String SYSTEM_ACCOUNT_ID = Constants.SYSTEM_ACCOUNT_ID;
    protected static final String YUNSU_ORG_ID = "2k0r1l55i2rs5544wz5";

    @LocalServerPort
    public int port;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;


    @Before
    public void initRestClient() {
        if (restClient == null) {
            System.out.println("initializing restClient");
            String appId = "AuthUnitTest";
            String deviceId = getHostName();
            String accessToken = tokenAuthenticationService.generateAccessToken(SYSTEM_ACCOUNT_ID, YUNSU_ORG_ID).getToken();
            restClient = new AsyncRestClient("http://localhost:" + port);
            restClient.setPreRequestCallback(request -> {
                HttpHeaders httpHeaders = request.getHeaders();
                httpHeaders.set(com.yunsoo.common.web.Constants.HttpHeaderName.APP_ID, appId);
                httpHeaders.set(com.yunsoo.common.web.Constants.HttpHeaderName.DEVICE_ID, deviceId);
                httpHeaders.set(com.yunsoo.common.web.Constants.HttpHeaderName.ACCESS_TOKEN, accessToken);
            });
        }
    }

    protected String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName().split("\\..")[0];
        } catch (UnknownHostException e) {
            return "Unknown";
        }
    }

}
