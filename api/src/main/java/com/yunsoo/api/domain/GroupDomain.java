package com.yunsoo.api.domain;

import com.yunsoo.api.client.DataAPIClient;
import com.yunsoo.common.data.object.AccountGroupObject;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.GroupObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/24
 * Descriptions:
 */
@Component
public class GroupDomain {

    @Autowired
    private DataAPIClient dataAPIClient;

    @Autowired
    private AccountDomain accountDomain;


    public GroupObject getById(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return null;
        }
        try {
            return dataAPIClient.get("group/{id}", GroupObject.class, groupId);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public List<GroupObject> getByOrgId(String orgId) {
        return dataAPIClient.get("group?org_id={orgId}", new ParameterizedTypeReference<List<GroupObject>>() {
        }, orgId);
    }

    public GroupObject create(GroupObject groupObject) {
        return dataAPIClient.post("group", groupObject, GroupObject.class);
    }

    public void patchUpdate(GroupObject groupObject) {
        dataAPIClient.patch("group/{id}", groupObject, groupObject.getId());
    }

    public void deleteGroupAndAllRelatedById(String groupId) {


    }

    public AccountGroupObject createAccountGroup(AccountGroupObject accountGroupObject) {
        return dataAPIClient.post("accountgroup", accountGroupObject, AccountGroupObject.class);
    }

    public List<AccountGroupObject> getAccountGroupByGroupid(String groupId) {
        return dataAPIClient.get("accountgroup?group_id={groupId}", new ParameterizedTypeReference<List<AccountGroupObject>>() {
        }, groupId);
    }

    public void deleteAccountGroup(String groupId, String accountId) {
        dataAPIClient.delete("accountgroup?group_id={group_id}&account_id={account_id}",groupId, accountId);
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
