package com.yunsoo.auth.api.security.authentication;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/10
 * Descriptions:
 */
public class TokenHandlerTest {

    private TokenHandler tokenHandler = new TokenHandler("nC9UYPUupMaViIG3UBODY7B19IYW6Z0X");

    @Test
    public void test_createLoginToken() {
        String accountId = "2lm7cm591fubaxwtdxk";
        String token = tokenHandler.createToken(DateTime.now().plusMinutes(525600000), accountId);
        System.out.println(token);
    }

    @Test
    public void tool_createAccessTokenForSystemAccount() {
        TokenHandler accessTokenHandler = new TokenHandler("lvtHDkfIUxJ2bLWHc0MNztUqCJSVPSJO");
        String accountId = "0010000000000000000";
        String orgId = "2k0r1l55i2rs5544wz5";
        String token = accessTokenHandler.createToken(DateTime.now().plusYears(20), accountId, orgId);
        System.out.println(token);
    }

}
