package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.AccountCreationRequest;
import com.yunsoo.auth.dto.AccountLoginResponse;
import com.yunsoo.auth.dto.Token;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by min on 7/29/16.
 */
public class LoginTokenControllerTest extends TestBase {

    private Account testAccount;

    private String testAccountLoginToken;

    @Before
    public void createAccount() {
        if (testAccount == null) {
            AccountCreationRequest rrequest = new AccountCreationRequest();
            rrequest.setOrgId("2k0r1l55i2rs5544wz5");
            rrequest.setIdentifier("testlogin");
            rrequest.setFirstName("Account for test");
            rrequest.setLastName("UT");
            rrequest.setPhone("123456789");
            rrequest.setPassword("test");
            testAccount = restClient.post("account", rrequest, Account.class);
        }
    }

    private void test_loginWithToken_200() {
        Token request = new Token();
        request.setToken(testAccountLoginToken);
        AccountLoginResponse response = restClient.post("login/token", request, AccountLoginResponse.class);
        assert response.getAccessToken().getExpiresIn() > 0;
    }

    @Test
    public void testGetLoginToken() throws Exception {
        testAccountLoginToken = restClient.get("loginToken?account_id={0}", Token.class, testAccount.getId()).getToken();
        test_loginWithToken_200();
    }

    @Test
    public void testGetLoginTokenWithExpiresIn() throws Exception {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("account_id", testAccount.getId())
                .append("expires_in", 3000).build();
        testAccountLoginToken = restClient.get("loginToken"+query, Token.class).getToken();
        test_loginWithToken_200();
    }

    @Test
    public void testGetLoginTokenWithExpiresInZeroSecond() throws Exception {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("account_id", testAccount.getId())
                .append("expires_in", 0).build();
        testAccountLoginToken = restClient.get("loginToken"+query, Token.class).getToken();
        test_loginWithToken_200();
    }
}