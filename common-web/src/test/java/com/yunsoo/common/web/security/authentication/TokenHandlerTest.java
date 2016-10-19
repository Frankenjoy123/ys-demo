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

}
