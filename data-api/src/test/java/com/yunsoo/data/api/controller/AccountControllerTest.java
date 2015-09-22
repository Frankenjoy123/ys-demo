package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.client.RestResponseErrorHandler;
import com.yunsoo.data.api.Application;
import com.yunsoo.data.api.Constants;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/9/22
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class AccountControllerTest {

    @Value("${local.server.port}")
    private int port;

    private RestClient dataAPIClient;

    @Before
    public void before() {
        dataAPIClient = new RestClient("http://localhost:" + port, new RestResponseErrorHandler());
    }

    @Test
    public void test_AccountController_All() {
        AccountObject accountObject = new AccountObject();
        accountObject.setOrgId(Constants.Ids.YUNSU_ORG_ID);
        accountObject.setIdentifier("unitTest");
        accountObject.setPhone("123456789");
        accountObject.setEmail("it@yunsu.co");
        accountObject.setFirstName("TestFirstName");
        accountObject.setLastName("TestLastName");
        accountObject.setStatusCode(LookupCodes.AccountStatus.AVAILABLE);
        accountObject.setCreatedAccountId(Constants.Ids.SYSTEM_ACCOUNT_ID);
        accountObject.setCreatedDateTime(DateTime.now());
        accountObject.setPassword("123456");
        accountObject.setHashSalt("TestHashSalt");

        accountObject = dataAPIClient.post("account", accountObject, AccountObject.class);
        String accountId = accountObject.getId();
        System.out.println("Account created with id: " + accountId);

        //get by id
        accountObject = dataAPIClient.get("account/{id}", AccountObject.class, accountId);
        assert accountObject.getId().equals(accountId);

        List<AccountObject> accountObjectList = dataAPIClient.get("account?org_id={0}&identifier={1}", new ParameterizedTypeReference<List<AccountObject>>() {
        }, accountObject.getOrgId(), accountObject.getIdentifier());
        assert accountObjectList.size() > 0 && accountObjectList.get(0).getId().equals(accountId);

        Long count = dataAPIClient.get("account/count?org_id={0}&status_code_in={1}", Long.class, accountObject.getOrgId(), accountObject.getStatusCode());
        System.out.println("Account count: " + count);
        assert count > 0;


    }

}
