package com.yunsoo.api.domain;

import com.yunsoo.api.client.DataApiClient1;
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
    private DataApiClient1 dataApiClient;

    @Autowired
    private AccountDomain accountDomain;

    @Autowired
    private GroupDomain groupDomain;


    public List<AccountGroupObject> getAccountGroupByGroupId(String groupId) {
        return dataApiClient.get("accountgroup?group_id={groupId}", new ParameterizedTypeReference<List<AccountGroupObject>>() {
        }, groupId);
    }

    public List<AccountGroupObject> getAccountGroupByAccountId(String accountId) {
        return dataApiClient.get("accountgroup?account_id={accountId}", new ParameterizedTypeReference<List<AccountGroupObject>>() {
        }, accountId);
    }

    public AccountGroupObject getAccountGroupByAccountIdAndGroupId(String accountId, String groupId) {
        List<AccountGroupObject> accountGroupObjects = dataApiClient.get("accountgroup?account_id={accountId}&group_id={groupId}", new ParameterizedTypeReference<List<AccountGroupObject>>() {
        }, accountId, groupId);
        if (accountGroupObjects.size() == 0) {
            return null;
        }
        return accountGroupObjects.get(0);
    }

    public void putAccountGroup(String accountId, String groupId) {
        AccountGroupObject obj = new AccountGroupObject();
        obj.setAccountId(accountId);
        obj.setGroupId(groupId);
        obj.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        obj.setCreatedDateTime(DateTime.now());
        dataApiClient.put("accountgroup", obj);
    }

    public void putAccountGroupsByAccount(AccountObject account, List<String> groupIds) {
        String accountId = account.getId();
        List<String> originalGroupIds = getAccountGroupByAccountId(accountId)
                .stream()
                .map(AccountGroupObject::getGroupId)
                .collect(Collectors.toList());
        List<String> allGroupIds = groupDomain.getByOrgId(account.getOrgId()).stream().map(GroupObject::getId).collect(Collectors.toList());
        //delete original but not in the new groupId list
        originalGroupIds.stream().filter(gId -> !groupIds.contains(gId)).forEach(gId -> {
            deleteAccountGroupByAccountIdAndGroupId(accountId, gId);
        });
        //add not in the original groupId list
        groupIds.stream()
                .filter(gId -> gId != null && allGroupIds.contains(gId) && !originalGroupIds.contains(gId))
                .forEach(gId -> {
                    putAccountGroup(accountId, gId);
                });
    }

    public void putAccountGroupsByGroup(GroupObject group, List<String> accountIds) {
        String groupId = group.getId();
        List<String> originalAccountIds = getAccountGroupByGroupId(groupId)
                .stream()
                .map(AccountGroupObject::getAccountId)
                .collect(Collectors.toList());
        List<String> allAccountIds = accountDomain.getByOrgId(group.getOrgId())
                .stream().map(AccountObject::getId).collect(Collectors.toList());
        //delete original but not in the new accountId list
        originalAccountIds.stream().filter(aId -> !accountIds.contains(aId)).forEach(aId -> {
            deleteAccountGroupByAccountIdAndGroupId(aId, groupId);
        });
        //add not in the original accountId list
        accountIds.stream()
                .filter(aId -> aId != null && allAccountIds.contains(aId) && !originalAccountIds.contains(aId))
                .forEach(aId -> {
                    putAccountGroup(aId, groupId);
                });
    }

    public void deleteAccountGroupByAccountIdAndGroupId(String accountId, String groupId) {
        dataApiClient.delete("accountgroup?account_id={account_id}&group_id={group_id}", accountId, groupId);
    }


    public List<GroupObject> getGroups(AccountObject accountObject) {
        List<AccountGroupObject> accountGroupObjects = dataApiClient.get("accountgroup?account_id={accountId}", new ParameterizedTypeReference<List<AccountGroupObject>>() {
        }, accountObject.getId());
        if (accountGroupObjects.size() == 0) {
            return new ArrayList<>();
        }
        List<String> groupIds = accountGroupObjects.stream().map(AccountGroupObject::getGroupId).collect(Collectors.toList());
        List<GroupObject> allGroups = groupDomain.getByOrgId(accountObject.getOrgId());
        return allGroups.stream().filter(g -> groupIds.contains(g.getId())).collect(Collectors.toList());
    }

    public List<AccountObject> getAccounts(GroupObject groupObject) {
        List<AccountGroupObject> accountGroupObjects = dataApiClient.get("accountgroup?group_id={groupId}", new ParameterizedTypeReference<List<AccountGroupObject>>() {
        }, groupObject.getId());
        if (accountGroupObjects.size() == 0) {
            return new ArrayList<>();
        }
        List<String> accountIds = accountGroupObjects.stream().map(AccountGroupObject::getAccountId).collect(Collectors.toList());
        List<AccountObject> allAccounts = accountDomain.getByOrgId(groupObject.getOrgId());
        return allAccounts.stream().filter(a -> accountIds.contains(a.getId())).collect(Collectors.toList());
    }

}
