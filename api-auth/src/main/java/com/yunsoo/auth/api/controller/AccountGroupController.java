package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.Group;
import com.yunsoo.auth.service.AccountGroupService;
import com.yunsoo.auth.service.AccountService;
import com.yunsoo.auth.service.GroupService;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-07-12
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/account/{account_id}/group")
public class AccountGroupController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private AccountGroupService accountGroupService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Group> getGroups(@PathVariable("account_id") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        Account account = findAccountById(accountId);
        if (!AuthUtils.isMe(accountId)) {
            AuthUtils.checkPermission(account.getOrgId(), "account_group", "read");
        }
        return accountGroupService.getGroupsByAccountId(accountId);
    }

    @RequestMapping(value = "{group_id}", method = RequestMethod.PUT)
    public void putAccountGroup(@PathVariable(value = "account_id") String accountId,
                                @PathVariable(value = "group_id") String groupId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        Account account = findAccountById(accountId);
        Group group = findGroupById(groupId);
        if (!account.getOrgId().equals(group.getOrgId())) {
            throw new BadRequestException("account and group are not in the same organization");
        }
        AuthUtils.checkPermission(account.getOrgId(), "account_group", "write");
        accountGroupService.putAccountGroup(accountId, groupId);
    }

    //update account group under the account
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void updateAccountGroups(@PathVariable(value = "account_id") String accountId,
                                    @RequestBody List<String> groupIds) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        Account account = findAccountById(accountId);
        AuthUtils.checkPermission(account.getOrgId(), "account_group", "write");
        accountGroupService.putAccountGroupsByAccount(account, groupIds);
    }

    //delete account group under the account
    @RequestMapping(value = "{group_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountGroup(@PathVariable(value = "account_id") String accountId,
                                   @PathVariable(value = "group_id") String groupId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        Account account = findAccountById(accountId);
        AuthUtils.checkPermission(account.getOrgId(), "account_group", "write");
        accountGroupService.deleteAccountGroupByAccountIdAndGroupId(accountId, groupId);
    }


    private Account findAccountById(String accountId) {
        Account account = accountService.getById(accountId);
        if (account == null) {
            throw new NotFoundException("account not found");
        }
        return account;
    }

    private Group findGroupById(String id) {
        Group group = groupService.getById(id);
        if (group == null) {
            throw new NotFoundException("group not found");
        }
        return group;
    }

}
