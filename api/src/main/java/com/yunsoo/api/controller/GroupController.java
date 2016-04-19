package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.AccountGroupDomain;
import com.yunsoo.api.domain.GroupDomain;
import com.yunsoo.api.dto.Account;
import com.yunsoo.api.dto.Group;
import com.yunsoo.api.dto.PermissionEntry;
import com.yunsoo.api.security.AuthAccount;
import com.yunsoo.api.security.authorization.AuthorizationService;
import com.yunsoo.api.security.permission.PermissionService;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.GroupObject;
import com.yunsoo.common.web.exception.BadRequestException;
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
    private AccountDomain accountDomain;

    @Autowired
    private GroupDomain groupDomain;

    @Autowired
    private AccountGroupDomain accountGroupDomain;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AuthorizationService authorizationService;

    //region group

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'group:read')")
    public Group getById(@PathVariable("id") String id) {
        GroupObject groupObject = findGroupById(id);
        return new Group(groupObject);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'group:read')")
    public List<Group> getByOrgId(@RequestParam(value = "org_id", required = false) String orgId) {
        orgId = AuthUtils.fixOrgId(orgId);
        return groupDomain.getByOrgId(orgId).stream().map(Group::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#group, 'group:create')")
    public Group create(@RequestBody @Valid Group group) {
        GroupObject groupObject = group.toGroupObject();
        AuthAccount currentAccount = AuthUtils.getCurrentAccount();

        groupObject.setId(null);
        groupObject.setOrgId(AuthUtils.fixOrgId(groupObject.getOrgId()));
        groupObject.setCreatedAccountId(currentAccount.getId());
        groupObject.setCreatedDateTime(DateTime.now());
        groupObject.setModifiedAccountId(null);
        groupObject.setModifiedDatetime(null);
        groupObject = groupDomain.create(groupObject);
        return new Group(groupObject);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable("id") String id, @RequestBody Group group) {
        GroupObject groupObject = findGroupById(id);
        AuthUtils.checkPermission(groupObject.getOrgId(), "group", "write");
        if (group.getName() != null) {
            groupObject.setName(group.getName());
        }
        if (group.getDescription() != null) {
            groupObject.setDescription(group.getDescription());
        }
        groupObject.setModifiedAccountId(AuthUtils.getCurrentAccount().getOrgId());
        groupObject.setModifiedDatetime(DateTime.now());
        groupDomain.patchUpdate(groupObject);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        groupDomain.deleteGroupAndAllRelatedById(id);
    }


    //endregion

    //region accounts

    @RequestMapping(value = "{id}/account", method = RequestMethod.GET)
    public List<Account> getAccounts(@PathVariable("id") String groupId) {
        GroupObject groupObject = findGroupById(groupId);
        AuthUtils.checkPermission(groupObject.getOrgId(), "account_group", "read");
        return accountGroupDomain.getAccounts(groupObject).stream().map(Account::new).collect(Collectors.toList());
    }

    //create account group under the group
    @RequestMapping(value = "{group_id}/account/{account_id}", method = RequestMethod.PUT)
    public void putAccountGroup(@PathVariable(value = "group_id") String groupId,
                                @PathVariable(value = "account_id") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        AccountObject accountObject = findAccountById(accountId);
        GroupObject groupObject = findGroupById(groupId);
        if (!accountObject.getOrgId().equals(groupObject.getOrgId())) {
            throw new BadRequestException("account and group are not in the same organization");
        }
        AuthUtils.checkPermission(accountObject.getOrgId(), "account_group", "write");
        accountGroupDomain.putAccountGroup(accountId, groupId);
    }

    //update account group under the group
    @RequestMapping(value = "{group_id}/account", method = RequestMethod.PUT)
    public void updateAccountGroups(@PathVariable(value = "group_id") String groupId,
                                    @RequestBody List<String> accountIds) {
        GroupObject group = findGroupById(groupId);
        AuthUtils.checkPermission(group.getOrgId(), "account_group", "write");
        if (accountIds.size() > 0) {
            accountGroupDomain.putAccountGroupsByGroup(group, accountIds);
        }
    }

    //delete account group under the group
    @RequestMapping(value = "{group_id}/account/{account_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountGroup(@PathVariable(value = "group_id") String groupId,
                                   @PathVariable(value = "account_id") String accountId) {
        GroupObject groupObject = findGroupById(groupId);
        AuthUtils.checkPermission(groupObject.getOrgId(), "account_group", "write");
        accountGroupDomain.deleteAccountGroupByAccountIdAndGroupId(accountId, groupId);
    }

    //endregion

    @RequestMapping(value = "{group_id}/permission", method = RequestMethod.GET)
    public List<PermissionEntry> getAllPermissionByGroupId(@PathVariable("group_id") String groupId) {
        GroupObject group = findGroupById(groupId);
        String orgId = group.getOrgId();
        AuthUtils.checkPermission(orgId, "permission_allocation", "read");
        List<com.yunsoo.api.security.permission.PermissionEntry> permissionEntries = permissionService.getExpendedPermissionEntriesByGroupId(groupId);
        //fix orgRestriction
        permissionEntries.forEach(p -> {
            p.setRestriction(authorizationService.fixOrgRestriction(p.getRestriction(), orgId));
        });
        return permissionEntries.stream().map(PermissionEntry::new).collect(Collectors.toList());
    }


    private AccountObject findAccountById(String accountId) {
        AccountObject accountObject = accountDomain.getById(accountId);
        if (accountObject == null) {
            throw new NotFoundException("account not found");
        }
        return accountObject;
    }

    private GroupObject findGroupById(String id) {
        GroupObject groupObject = groupDomain.getById(id);
        if (groupObject == null) {
            throw new NotFoundException("group not found");
        }
        return groupObject;
    }

}
