package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.AccountCreationRequest;
import com.yunsoo.auth.dto.Group;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by min on 8/1/16.
 */
public class AccountGroupControllerTest extends TestBase {

    private List<String> groupIds;
    private String accountId;

    @Before
    public void createGroup() {
        //create 1 account
        System.out.println("this is the account id ");
        AccountCreationRequest r = new AccountCreationRequest();
        r.setOrgId(YUNSU_ORG_ID);
        r.setIdentifier("test");
        r.setFirstName("Account");
        r.setLastName("UT");
        r.setPhone("123456789");
        r.setPassword("test");
        Account account = restClient.post("account", r, Account.class);
        accountId = account.getId();

        groupIds = IntStream.range(0, 4).parallel().mapToObj(i -> {
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
    }

    private long getGroupsSize() {
        List<Group> list = restClient.get("account/{account_id}/group", new ParameterizedTypeReference<List<Group>>() {
        }, accountId);
        return list.size();
    }

    private void putAccountGroup(String groupId) {
        restClient.put("account/{account_id}/group/{group_id}", (Object)null, accountId, groupId);
    }

    @Test
    public void testGetGroups() throws Exception {
        assert  getGroupsSize() == 0;
    }

    @Test
    public void testPutAccountGroup() throws Exception {
        System.out.println("testPutAccountGroup size is " + getGroupsSize());

        System.out.println("account id is " + accountId);
        putAccountGroup(groupIds.get(0));

        assert  getGroupsSize() == 1;
        System.out.println("finished testPutAccountGroup size is " + getGroupsSize());

    }

    @Test
    public void testUpdateAccountGroups() throws Exception {
        System.out.println("testUpdateAccountGroups size is " + getGroupsSize());
        IntStream.range(0, 3).parallel().forEach(i -> putAccountGroup(groupIds.get(i)));
        assert getGroupsSize() == 3;
        putAccountGroup(groupIds.get(3));
        assert getGroupsSize() == 4;
        System.out.println("finished testUpdateAccountGroups size is " + getGroupsSize());
    }

    @Test
    public void testDeleteAccountGroup() throws Exception {
        System.out.println("testDeleteAccountGroup size is " + getGroupsSize());
        IntStream.range(0, 4).parallel().forEach(i -> putAccountGroup(groupIds.get(i)));
        assert getGroupsSize() == 4;
        restClient.delete("account/{account_id}/group/{group_id}", accountId, groupIds.get(2));
        List<Group> list = restClient.get("account/{account_id}/group", new ParameterizedTypeReference<List<Group>>() {
        }, accountId);
        List<String> ids = list.stream().map(Group::getId).collect(Collectors.toList());
        if (ids.contains(groupIds.get(2))) throw new AssertionError();
        assert ids.contains(groupIds.get(0));
        System.out.println("finished testDeleteAccountGroup size is " + getGroupsSize());
    }
}