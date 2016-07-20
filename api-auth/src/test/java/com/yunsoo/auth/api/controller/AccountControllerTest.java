package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.Account;
import org.junit.Test;

/**
 * Created by:   Lijian
 * Created on:   2016-07-13
 * Descriptions:
 */
public class AccountControllerTest extends TestBase {

    @Test
    public void test_All() {
        Account account = restClient.get("account/{id}", Account.class, Constants.SYSTEM_ACCOUNT_ID);
        System.out.println(account.getLastName() + account.getFirstName());
    }
}
