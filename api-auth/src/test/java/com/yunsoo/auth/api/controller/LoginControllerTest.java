package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.*;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by:   Lijian
 * Created on:   2016-07-11
 * Descriptions:
 */
public class LoginControllerTest extends TestBase {

    private static Account testAccount;

    private static Account disabledAccount;

    private static String testAccountLoginToken;

    private static String disabledAccountLoginToken;

    private String accountIdentifier = "testlogin" + RandomUtils.generateString(4);

    @Before
    public void createAccount() {
        if (testAccount == null) {
            System.out.println("init test account");
            AccountCreationRequest request = new AccountCreationRequest();
            request.setOrgId("2k0r1l55i2rs5544wz5");
            request.setIdentifier(accountIdentifier);
            request.setFirstName("Account for test");
            request.setLastName("UT");
            request.setPhone("123456789");
            request.setPassword("test");
            testAccount = restClient.post("account", request, Account.class);
            testAccountLoginToken = restClient.get("loginToken?account_id={0}", Token.class, testAccount.getId()).getToken();

            System.out.println("account id is " + testAccount.getId() + "identifier is" + accountIdentifier);
            request.setIdentifier("testlogin_disabled");
            disabledAccount = restClient.post("account", request, Account.class);
            disabledAccountLoginToken = restClient.get("loginToken?account_id={0}", Token.class, disabledAccount.getId()).getToken();
            restClient.post("account/{id}/disable", null, null, disabledAccount.getId());
        }
    }

    //region login with password

    @Test
    public void test_loginWithPassword_200() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setOrganization("2k0r1l55i2rs5544wz5");
        request.setIdentifier(accountIdentifier);
        request.setPassword("test");
        AccountLoginResponse response = restClient.post("login/password", request, AccountLoginResponse.class);
        assert response.getAccessToken().getExpiresIn() > 0;
    }

    @Test(expected = UnauthorizedException.class)
    public void test_loginWithPassword_401_wrongPassword() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setOrganization("2k0r1l55i2rs5544wz5");
        request.setIdentifier("testlogin");
        request.setPassword("test_wrong");
        System.out.println("testing wrong password");
        restClient.post("login/password", request, AccountLoginResponse.class);
    }

    @Test(expected = UnauthorizedException.class)
    public void test_loginWithPassword_401_wrongIdentifier() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setOrganization("2k0r1l55i2rs5544wz5");
        request.setIdentifier("testlogin_wrong");
        request.setPassword("test");
        restClient.post("login/password", request, AccountLoginResponse.class);
    }

    @Test(expected = UnauthorizedException.class)
    public void test_loginWithPassword_401_wrongOrg() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setOrganization("12345Wrong");
        request.setIdentifier("testlogin");
        request.setPassword("test");
        restClient.post("login/password", request, AccountLoginResponse.class);
    }

    @Test
    public void test_loginWithPassword_200_accountId() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setAccountId(testAccount.getId());
        request.setPassword("test");
        AccountLoginResponse response = restClient.post("login/password", request, AccountLoginResponse.class);
        assert response.getAccessToken().getExpiresIn() > 0;
    }

    @Test(expected = UnauthorizedException.class)
    public void test_loginWithPassword_401_accountId_wrongPassword() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setAccountId(testAccount.getId());
        request.setPassword("test_wrong");
        restClient.post("login/password", request, AccountLoginResponse.class);
    }

    @Test(expected = UnauthorizedException.class)
    public void test_loginWithPassword_401_accountId_wrongAccountId() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setAccountId("not_exists_id");
        request.setPassword("test");
        restClient.post("login/password", request, AccountLoginResponse.class);
    }

    @Test(expected = BadRequestException.class)
    public void test_loginWithPassword_404_nullOrg() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setIdentifier("testlogin");
        request.setPassword("test");
        restClient.post("login/password", request, AccountLoginResponse.class);
    }

    @Test(expected = BadRequestException.class)
    public void test_loginWithPassword_404_emptyOrg() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setOrganization("");
        request.setIdentifier("testlogin");
        request.setPassword("test");
        restClient.post("login/password", request, AccountLoginResponse.class);
    }

    @Test(expected = BadRequestException.class)
    public void test_loginWithPassword_404_nullIdentifier() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setOrganization("2k0r1l55i2rs5544wz5");
        request.setPassword("test");
        restClient.post("login/password", request, AccountLoginResponse.class);
    }

    @Test(expected = BadRequestException.class)
    public void test_loginWithPassword_404_emptyIdentifier() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setOrganization("2k0r1l55i2rs5544wz5");
        request.setIdentifier("");
        request.setPassword("test");
        restClient.post("login/password", request, AccountLoginResponse.class);
    }

    @Test(expected = BadRequestException.class)
    public void test_loginWithPassword_404_nullPassword() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setOrganization("2k0r1l55i2rs5544wz5");
        request.setIdentifier("testlogin");
        restClient.post("login/password", request, AccountLoginResponse.class);
    }

    @Test(expected = BadRequestException.class)
    public void test_loginWithPassword_404_emptyPassword() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setOrganization("2k0r1l55i2rs5544wz5");
        request.setIdentifier("testlogin");
        request.setPassword("");
        restClient.post("login/password", request, AccountLoginResponse.class);
    }

    @Test(expected = BadRequestException.class)
    public void test_loginWithPassword_404_emptyAccount() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setAccountId("");
        request.setPassword("");
        restClient.post("login/password", request, AccountLoginResponse.class);
    }

    @Test(expected = BadRequestException.class)
    public void test_loginWithPassword_404_null() {
        restClient.post("login/password", null, AccountLoginResponse.class);
    }


    //endregion


    //region login with login token

    @Test
    public void test_loginWithToken_200() {
        Token request = new Token();
        request.setToken(testAccountLoginToken);
        AccountLoginResponse response = restClient.post("login/token", request, AccountLoginResponse.class);
        assert response.getAccessToken().getExpiresIn() > 0;
    }

    @Test(expected = BadRequestException.class)
    public void test_loginWithToken_400() {
        Token request = new Token();
        request.setToken("");
        restClient.post("login/token", request, AccountLoginResponse.class);
    }

    //endregion

    //region test disabled account login

    @Test(expected = UnauthorizedException.class)
    public void test_disabledAccount_401_withPassword() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setOrganization("2k0r1l55i2rs5544wz5");
        request.setIdentifier("testlogin_disabled");
        request.setPassword("test");
        restClient.post("login/password", request, AccountLoginResponse.class);
    }

    @Test(expected = UnauthorizedException.class)
    public void test_disabledAccount_401_withPassword_accountId() {
        AccountLoginRequest request = new AccountLoginRequest();
        request.setAccountId(disabledAccount.getId());
        request.setPassword("test");
        restClient.post("login/password", request, AccountLoginResponse.class);
    }

    @Test(expected = UnauthorizedException.class)
    public void test_disabledAccount_401_withToken() {
        Token request = new Token();
        request.setToken(disabledAccountLoginToken);
        restClient.post("login/token", request, AccountLoginResponse.class);
    }

    //endregion

}
