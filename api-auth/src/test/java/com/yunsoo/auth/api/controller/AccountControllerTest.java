package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.AccountCreationRequest;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by:   Lijian
 * Created on:   2016-07-13
 * Descriptions:
 */
public class AccountControllerTest extends TestBase {

    @Test
    public void test_get() {
        //get account
        Account account = restClient.get("account/{id}", Account.class, Constants.SYSTEM_ACCOUNT_ID);
        System.out.println(account.getLastName() + account.getFirstName());
    }

    @Test
    public void test_create() {
        //async create 100 accounts
        IntStream.range(0, 100).mapToObj(i -> {
            AccountCreationRequest r = new AccountCreationRequest();
            r.setOrgId("2k0r1l55i2rs5544wz5");
            r.setIdentifier("test" + i);
            r.setFirstName("Account" + i);
            r.setLastName("UT");
            r.setPhone("123456789");
            r.setPassword("test");
            return restClient.postAsync("account", r, Account.class);
        }).map(f -> {
            try {
                Account a = f.get();
                System.out.println(a.getId() + " " + a.getFirstName());
                return a;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

    }
}
