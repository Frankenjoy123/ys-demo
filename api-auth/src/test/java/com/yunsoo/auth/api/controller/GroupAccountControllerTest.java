package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.AccountCreationRequest;
import com.yunsoo.auth.dto.Group;
import com.yunsoo.common.util.RandomUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by min on 7/28/16.
 */
public class GroupAccountControllerTest extends TestBase {

    private List<String> accountIds;
    private String groupId;
    private String accountIdentifier = "testAccount" + RandomUtils.generateString(4);

    @Before
    public void createGroup() {
        //async create 4 groupIds
        List<String> groupIds = IntStream.range(0, 4).parallel().mapToObj(i -> {
            Group group = new Group();
            group.setOrgId(YUNSU_ORG_ID);
            group.setName("Group" + i);
            group.setDescription("good group");
            group.setCreatedAccountId(Constants.SYSTEM_ACCOUNT_ID);
            group.setCreatedDateTime(DateTime.now());
            return restClient.postAsync("group", group, Group.class);
        }).map(f -> {
            try {
                Group a = f.get();
                System.out.println(a + " " + a.getName());
                return a.getId();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        groupId = groupIds.get(2);

        //async create 8 accountIds
        accountIds = IntStream.range(0, 8).parallel().mapToObj(i -> {
            AccountCreationRequest r = new AccountCreationRequest();
            r.setOrgId(YUNSU_ORG_ID);
            r.setIdentifier(accountIdentifier + i);
            r.setFirstName("Account" + i);
            r.setLastName("UT");
            r.setPhone("123456789");
            r.setPassword("test");
            return restClient.postAsync("account", r, Account.class);
        }).map(f -> {
            try {
                Account a = f.get();
                System.out.println(a + " " + a.getFirstName());
                return a.getId();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }

    private void putAccountGroup(String group_id, String account_id) {
        restClient.put("group/{group_id}/account/{account_id}", (Object) null, group_id, account_id);
    }

    private void deleteAccountGroup(String group_id, String account_id) {
        restClient.delete("group/{group_id}/account/{account_id}", group_id, account_id);
    }

    private List<Account> getAccounts(String group_id) {
        return restClient.get("group/{group_id}/account/", new ParameterizedTypeReference<List<Account>>() {
        }, group_id);
    }

    @Test
    public void testGetAccounts() throws Exception {
        assert getAccounts(groupId).size() == 0;
    }

    @Test
    public void testPutAccountGroup() throws Exception {
        assert getAccounts(groupId).size() == 0;
        putAccountGroup(groupId, accountIds.get(0));
        assert getAccounts(groupId).size() == 1;
    }

    @Test
    public void testUpdateAccountGroup() throws Exception {
        assert getAccounts(groupId).size() == 0;
        restClient.put("group/{group_id}/account/", accountIds, groupId);
        assert getAccounts(groupId).size() == 8;
    }

    @Test
    public void testDeleteAccountGroup() throws Exception {
        testUpdateAccountGroup();
        deleteAccountGroup(groupId, accountIds.get(0));
        assert getAccounts(groupId).size() == 7;
    }
}