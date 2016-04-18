package com.yunsoo.api.domain;

import com.yunsoo.api.client.DataAPIClient;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.AccountGroupObject;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.GroupObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/8
 * Descriptions:
 */
@Component
public class AccountGroupDomain {

    @Autowired
    private DataAPIClient dataAPIClient;

    @Autowired
    private AccountDomain accountDomain;

    @Autowired
    private GroupDomain groupDomain;


    public List<AccountGroupObject> getAccountGroupByGroupId(String groupId) {
        return dataAPIClient.get("accountgroup?group_id={groupId}", new ParameterizedTypeReference<List<AccountGroupObject>>() {
        }, groupId);
    }

    public List<AccountGroupObject> getAccountGroupByAccountId(String accountId) {
        return dataAPIClient.get("accountgroup?account_id={accountId}", new ParameterizedTypeReference<List<AccountGroupObject>>() {
        }, accountId);
    }

    public AccountGroupObject getAccountGroupByAccountIdAndGroupId(String accountId, String groupId) {
        List<AccountGroupObject> accountGroupObjects = dataAPIClient.get("accountgroup?account_id={accountId}&group_id={groupId}", new ParameterizedTypeReference<List<AccountGroupObject>>() {
        }, accountId, groupId);
        if (accountGroupObjects.size() == 0) {
            return null;
        }
        return accountGroupObjects.get(0);
    }

    public AccountGroupObject createAccountGroup(AccountGroupObject accountGroupObject) {
        return dataAPIClient.post("accountgroup", accountGroupObject, AccountGroupObject.class);
    }

    public void putAccountGroup(String accountId, String groupId) {
        AccountGroupObject obj = new AccountGroupObject();
        obj.setAccountId(accountId);
        obj.setGroupId(groupId);
        obj.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        obj.setCreatedDateTime(DateTime.now());
        dataAPIClient.put("accountgroup", obj);
    }

    public void deleteAccountGroupByAccountIdAndGroupId(String accountId, String groupId) {
        dataAPIClient.delete("accountgroup?account_id={account_id}&group_id={group_id}", accountId, groupId);
    }


    public List<GroupObject> getGroups(AccountObject accountObject) {
        List<AccountGroupObject> accountGroupObjects = dataAPIClient.get("accountgroup?account_id={accountId}", new ParameterizedTypeReference<List<AccountGroupObject>>() {
        }, accountObject.getId());
        if (accountGroupObjects.size() == 0) {
            return new ArrayList<>();
        }
        List<GroupObject> allGroups = groupDomain.getByOrgId(accountObject.getOrgId());
        List<String> groupIds = accountGroupObjects.stream().map(AccountGroupObject::getGroupId).collect(Collectors.toList());
        return allGroups.stream().filter(g -> groupIds.contains(g.getId())).collect(Collectors.toList());
    }

    public List<AccountObject> getAccounts(GroupObject groupObject) {
        List<AccountGroupObject> accountGroupObjects = dataAPIClient.get("accountgroup?group_id={groupId}", new ParameterizedTypeReference<List<AccountGroupObject>>() {
        }, groupObject.getId());
        if (accountGroupObjects.size() == 0) {
            return new ArrayList<>();
        }
        List<String> accountIds = accountGroupObjects.stream().map(AccountGroupObject::getAccountId).collect(Collectors.toList());
        List<AccountObject> allAccounts = accountDomain.getByOrgId(groupObject.getOrgId(), null).getContent();
        return allAccounts.stream().filter(a -> accountIds.contains(a.getId())).collect(Collectors.toList());
    }

}
