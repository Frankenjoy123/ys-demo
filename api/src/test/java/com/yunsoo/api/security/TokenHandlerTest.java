package com.yunsoo.api.security;

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
}
