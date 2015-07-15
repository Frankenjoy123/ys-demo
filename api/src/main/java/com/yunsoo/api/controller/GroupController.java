package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountGroupDomain;
import com.yunsoo.api.domain.GroupDomain;
import com.yunsoo.api.domain.GroupPermissionDomain;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.AccountGroupObject;
import com.yunsoo.common.data.object.GroupObject;
import com.yunsoo.common.data.object.GroupPermissionObject;
import com.yunsoo.common.data.object.GroupPermissionPolicyObject;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/24
 * Descriptions:
 */
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupDomain groupDomain;

    @Autowired
    private AccountGroupDomain accountGroupDomain;

    @Autowired
    private GroupPermissionDomain groupPermissionDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;


    @PostAuthorize("hasPermission(returnObject, 'group:read')")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Group getById(@PathVariable("id") String id) {
        GroupObject groupObject = findGroupById(id);
        return new Group(groupObject);
    }

    @PreAuthorize("hasPermission(#orgId, 'filterByOrg', 'group:read')")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Group> getByOrgId(@RequestParam(value = "org_id", required = false) String orgId) {
        if (orgId == null) {
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return groupDomain.getByOrgId(orgId).stream().map(Group::new).collect(Collectors.toList());
    }

    @PreAuthorize("hasPermission(#group, 'group:create')")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Group create(@RequestBody @Valid Group group) {
        GroupObject groupObject = group.toGroupObject();
        TAccount currentAccount = tokenAuthenticationService.getAuthentication().getDetails();

        groupObject.setId(null);
        if (groupObject.getOrgId() == null) {
            groupObject.setOrgId(currentAccount.getOrgId());
        }
        groupObject.setCreatedAccountId(currentAccount.getId());
        groupObject.setCreatedDateTime(DateTime.now());
        groupObject.setModifiedAccountId(null);
        groupObject.setModifiedDatetime(null);
        groupObject = groupDomain.create(groupObject);
        return new Group(groupObject);
    }

    @PreAuthorize("hasPermission(#group, 'group:modify')")
    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable("id") String id, @RequestBody Group group) {
        GroupObject groupObject = findGroupById(id);
        if (group.getName() != null) {
            groupObject.setName(group.getName());
        }
        if (group.getDescription() != null) {
            groupObject.setDescription(group.getDescription());
        }
        groupObject.setModifiedAccountId(tokenAuthenticationService.getAuthentication().getDetails().getOrgId());
        groupObject.setModifiedDatetime(DateTime.now());
        groupDomain.patchUpdate(groupObject);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        groupDomain.deleteGroupAndAllRelatedById(id);
    }


    //accounts

    @RequestMapping(value = "{id}/account", method = RequestMethod.GET)
    public List<Account> getAccounts(@PathVariable("id") String groupId) {
        GroupObject groupObject = groupDomain.getById(groupId);
        if (groupObject == null) {
            throw new NotFoundException("group not found");
        }
        return accountGroupDomain.getAccounts(groupObject).stream().map(Account::new).collect(Collectors.toList());
    }

    //create account group under the group
    @RequestMapping(value = "{group_id}/account/{account_id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createAccountGroup(@PathVariable(value = "group_id") String groupId,
                                     @PathVariable(value = "account_id") String accountId) {
        findGroupById(groupId);
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        AccountGroupObject exists = accountGroupDomain.getAccountGroupByAccountIdAndGroupId(accountId, groupId);
        if (exists != null) {
            throw new ConflictException("account id: " + accountId + "group id: " + groupId + "already exist.");
        }
        AccountGroupObject accountGroupObject = new AccountGroupObject();
        accountGroupObject.setAccountId(accountId);
        accountGroupObject.setGroupId(groupId);
        accountGroupObject.setCreatedAccountId(currentAccountId);
        accountGroupObject.setCreatedDateTime(DateTime.now());
        accountGroupDomain.createAccountGroup(accountGroupObject);
        return accountId;
    }

    //delete account group under the group
    @RequestMapping(value = "{group_id}/account/{account_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountGroup(@PathVariable(value = "group_id") String groupId,
                                   @PathVariable(value = "account_id") String accountId) {
        accountGroupDomain.deleteAccountGroupByAccountIdAndGroupId(accountId, groupId);
    }

    //update account group under the group
    @RequestMapping(value = "{group_id}/account", method = RequestMethod.PUT)
    public void updateAccountGroup(@PathVariable(value = "group_id") String groupId,
                                   @RequestBody List<String> accountIds) {
        findGroupById(groupId);
        List<String> originalAccountIds = accountGroupDomain.getAccountGroupByGroupId(groupId).stream()
                .map(AccountGroupObject::getAccountId).collect(Collectors.toList());
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        //delete original but not in the new accountId list
        originalAccountIds.stream().filter(aId -> !accountIds.contains(aId)).forEach(aId -> {
            accountGroupDomain.deleteAccountGroupByAccountIdAndGroupId(aId, groupId);
        });
        //add not in the original accountId list
        accountIds.stream().filter(aId -> !originalAccountIds.contains(aId)).forEach(aId -> {
            AccountGroupObject accountGroupObject = new AccountGroupObject();
            accountGroupObject.setAccountId(aId);
            accountGroupObject.setGroupId(groupId);
            accountGroupObject.setCreatedAccountId(currentAccountId);
            accountGroupObject.setCreatedDateTime(DateTime.now());
            accountGroupDomain.createAccountGroup(accountGroupObject);
        });
    }

    //region grouppermission

    @RequestMapping(value = "{group_id}/grouppermission", method = RequestMethod.GET)
    public List<GroupPermission> getPermissionsByGroupId(@PathVariable(value = "group_id") String groupId) {
        findGroupById(groupId);
        return groupPermissionDomain.getGroupPermissions(groupId)
                .stream()
                .map(GroupPermission::new)
                .collect(Collectors.toList());
    }

    //create group permission
    @RequestMapping(value = "{group_id}/grouppermission", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public GroupPermission createGroupPermission(@PathVariable(value = "group_id") String groupId,
                                                 @RequestBody @Valid GroupPermission groupPermission) {
        findGroupById(groupId);
        GroupPermissionObject groupPermissionObject = groupPermission.toGroupPermissionObject();
        TAccount currentAccount = tokenAuthenticationService.getAuthentication().getDetails();
        groupPermissionObject.setId(null);
        groupPermissionObject.setGroupId(groupId);
        groupPermissionObject.setOrgId(fixOrgId(groupPermissionObject.getOrgId()));
        groupPermissionObject.setCreatedAccountId(currentAccount.getId());
        groupPermissionObject.setCreatedDatetime(DateTime.now());
        groupPermissionObject = groupPermissionDomain.createGroupPermission(groupPermissionObject);
        return new GroupPermission(groupPermissionObject);
    }

    //delete group permission
    @RequestMapping(value = "{group_id}/grouppermission/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGroupPermission(@PathVariable(value = "group_id") String groupId,
                                      @PathVariable("id") String id) {
        findGroupById(groupId);
        groupPermissionDomain.deleteGroupPermissionById(id);
    }

    //endregion


    //region grouppermissionpolicy

    @RequestMapping(value = "{group_id}/grouppermissionpolicy", method = RequestMethod.GET)
    public List<GroupPermissionPolicy> getPermissionPoliciesByGroupId(@PathVariable(value = "group_id") String groupId) {
        findGroupById(groupId);
        return groupPermissionDomain.getGroupPermissionPolicies(groupId)
                .stream()
                .map(GroupPermissionPolicy::new)
                .collect(Collectors.toList());
    }

    //create group permission policy
    @RequestMapping(value = "{group_id}/grouppermissionpolicy", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public GroupPermissionPolicy createGroupPermissionPolicy(@PathVariable(value = "group_id") String groupId,
                                                             @RequestBody @Valid GroupPermissionPolicy groupPermissionPolicy) {
        findGroupById(groupId);
        GroupPermissionPolicyObject groupPermissionPolicyObject = groupPermissionPolicy.toGroupPermissionPolicyObject();
        TAccount currentAccount = tokenAuthenticationService.getAuthentication().getDetails();
        groupPermissionPolicyObject.setId(null);
        groupPermissionPolicyObject.setGroupId(groupId);
        groupPermissionPolicyObject.setOrgId(fixOrgId(groupPermissionPolicyObject.getOrgId()));
        groupPermissionPolicyObject.setCreatedAccountId(currentAccount.getId());
        groupPermissionPolicyObject.setCreatedDatetime(DateTime.now());
        groupPermissionPolicyObject = groupPermissionDomain.createGroupPermissionPolicy(groupPermissionPolicyObject);
        return new GroupPermissionPolicy(groupPermissionPolicyObject);
    }

    //delete group permission policy
    @RequestMapping(value = "{group_id}/grouppermissionpolicy/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGroupPermissionPolicy(@PathVariable(value = "group_id") String groupId,
                                            @PathVariable("id") String id) {
        findGroupById(groupId);
        groupPermissionDomain.deleteGroupPermissionPolicyById(id);
    }

    //endregion

    @RequestMapping(value = "{group_id}/permission", method = RequestMethod.GET)
    public List<PermissionInstance> getAllPermissionByGroupId(@PathVariable("group_id") String groupId) {
        findGroupById(groupId);
        return groupPermissionDomain.getAllGroupPermissions(groupId);
    }


    private GroupObject findGroupById(String id) {
        GroupObject groupObject = groupDomain.getById(id);
        if (groupObject == null) {
            throw new NotFoundException("group not found");
        }
        return groupObject;
    }

    private String fixOrgId(String orgId) {
        if (orgId == null || "current".equals(orgId)) {
            //current orgId
            return tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return orgId;
    }

}
