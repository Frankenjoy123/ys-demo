package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.*;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by min on 8/2/16.
 */
public class AccessTokenControllerTest extends TestBase {

    private String permanentToken;
    private String accessToken;
    private String accountIdentifier = "testAccount" + RandomUtils.generateString(4);

    @Before
    public void createPermanentToken() {
        createAccount();
        AccountLoginRequest request = new AccountLoginRequest();
        request.setOrganization("2k0r1l55i2rs5544wz5");
        request.setIdentifier(accountIdentifier);
        request.setPassword("test");
        AccountLoginResponse response = restClient.post("login/password", request, AccountLoginResponse.class);
        Token token = response.getPermanentToken();
        permanentToken = token.getToken();
        token = response.getAccessToken();
        accessToken = token.getToken();
    }

    public void createAccount() {
        AccountCreationRequest rrequest = new AccountCreationRequest();
        rrequest.setOrgId("2k0r1l55i2rs5544wz5");
        rrequest.setIdentifier(accountIdentifier);
        rrequest.setFirstName("Account for test");
        rrequest.setLastName("UT");
        rrequest.setPhone("123456789");
        rrequest.setPassword("test");
        restClient.post("account", rrequest, Account.class);
    }

    @Test
    public void testGetAccessToken() throws Exception {
        Token token = restClient.get("accesstoken?permanent_token={permanent_token}", Token.class, permanentToken);
        System.out.println("token is " + token.getToken());
    }

    @Test(expected = UnauthorizedException.class)
    public void testGetAccessToken_wrongToken_401() throws Exception {
        restClient.get("accesstoken?permanent_token={permanent_token}", Token.class, "permanent_token");
    }

    @Test
    public void testParseAccessToken() throws Exception {
        Account account = restClient.post("accesstoken", accessToken, Account.class);
        System.out.println("account is "+account.getId());
    }

    @Test(expected = UnauthorizedException.class)
    public void testParseAccessToken_wrongToken_401() throws Exception {
        restClient.post("accesstoken", permanentToken, Account.class);
    }
}