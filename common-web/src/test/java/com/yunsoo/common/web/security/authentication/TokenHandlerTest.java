package com.yunsoo.common.web.security.authentication;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/10
 * Descriptions:
 */
public class TokenHandlerTest {

    @Test
    public void test_createToken() {
        TokenHandler tokenHandler = new TokenHandler("lvtHDkfIUxJ2bLWHc0MNztUqCJSVPSJO");
        String token = tokenHandler.createToken(DateTime.now().plusMinutes(20), "a", "b");
        System.out.println(token);
        String[] values = tokenHandler.parseToken(token);
        assert values != null : "token invalid";
        System.out.println(Arrays.asList(values));

    }

    @Test
    public void test_parseToken() {
        TokenHandler tokenHandler = new TokenHandler("lvtHDkfIUxJ2bLWHc0MNztUqCJSVPSJO");
        String token = "IGHXxgXYIO3yaWb3zvWgZW1smloFu42snAB0lG9KRDV8MW40NzF0NnBrLDAwMTAwMDAwMDAwMDAwMDAwMDAsMmswcjFsNTVpMnJzNTU0NHd6NQ";
        String[] values = tokenHandler.parseToken(token);
        assert values != null : "token invalid";
        System.out.println(Arrays.asList(values));
    }

    @Test
    public void test_createAuthAccountToken() {
        TokenHandler tokenHandler = new TokenHandler("lvtHDkfIUxJ2bLWHc0MNztUqCJSVPSJO");
        String token = tokenHandler.createToken(DateTime.now().plusMinutes(1), "123", "456", "few:23", "fe:", ":45");
        System.out.println(token);
        String[] values = tokenHandler.parseToken(token);
        assert values != null : "token invalid";
        System.out.println(Arrays.asList(values));
        AuthAccount authAccount = tokenHandler.parseTokenAsAuthAccount(token);
        System.out.println(authAccount.getId());
        System.out.println(authAccount.getOrgId());
        System.out.println(authAccount.getDetails());
    }

    @Test
    public void createAccessTokenForSystemAccount() {
        TokenHandler tokenHandler = new TokenHandler("lvtHDkfIUxJ2bLWHc0MNztUqCJSVPSJO");
        String token = tokenHandler.createToken(DateTime.now().plusYears(100), "0010000000000000000", "2k0r1l55i2rs5544wz5");
        System.out.println(token);
        String[] values = tokenHandler.parseToken(token);
        assert values != null : "token invalid";
        System.out.println(Arrays.asList(values));
    }

}
