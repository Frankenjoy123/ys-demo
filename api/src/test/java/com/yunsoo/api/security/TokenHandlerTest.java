package com.yunsoo.api.security;

import com.yunsoo.api.object.TAccount;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/10
 * Descriptions:
 */
public class TokenHandlerTest {

    private TokenHandler tokenHandler = new TokenHandler("LEmrmtPfWc1txs9uC9A7PevSVSbBbQL0");

    @Test
    public void test_createLoginToken() {
        String accountId = "2kadmvn8uh248k5k7wa";
        String token = tokenHandler.createToken(DateTime.now().plusMinutes(10), accountId, "test");
        System.out.println(token);
        TAccount account = tokenHandler.parseToken(token);
        if (account != null) {
            System.out.println(account.getId());
            System.out.println(account.getOrgId());
            System.out.println(account.getExpires());
        }
    }
}
