package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.data.api.Constants;
import com.yunsoo.data.api.ControllerTestBase;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/9/22
 * Descriptions:
 */
public class AccountControllerTest extends ControllerTestBase {

    @Test
    public void test_SystemAccount() {
        AccountObject accountObject = dataApiClient.get("account/{id}", AccountObject.class, Constants.Ids.SYSTEM_ACCOUNT_ID);
        assert accountObject.getOrgId().equals(Constants.Ids.YUNSU_ORG_ID);
    }

    @Test
    public void test_All() {
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

        accountObject = dataApiClient.post("account", accountObject, AccountObject.class);
        String accountId = accountObject.getId();
        System.out.println("Account created with id: " + accountId);

        //get by id
        accountObject = dataApiClient.get("account/{id}", AccountObject.class, accountId);
        assert accountObject.getId().equals(accountId);

        List<AccountObject> accountObjectList = dataApiClient.get("account?org_id={0}&identifier={1}", new ParameterizedTypeReference<List<AccountObject>>() {
        }, accountObject.getOrgId(), accountObject.getIdentifier());
        assert accountObjectList.size() > 0 && accountObjectList.get(0).getId().equals(accountId);

        Long count = dataApiClient.get("account/count?org_id={0}&status_code_in={1}", Long.class, accountObject.getOrgId(), accountObject.getStatusCode());
        System.out.println("Account count: " + count);
        assert count > 0;

        accountObject.setPhone("987654321");
        dataApiClient.patch("account/{id}", accountObject, accountObject.getId());

        accountObject = dataApiClient.get("account/{id}", AccountObject.class, accountId);
        assert accountObject.getPhone().equals("987654321");

    }

}
