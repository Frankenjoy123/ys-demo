package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.AccountGroupDomain;
import com.yunsoo.api.domain.GroupDomain;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.security.authorization.AuthorizationService;
import com.yunsoo.api.security.permission.PermissionService;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.GroupObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Zhe on 2015/3/5.
 * Handle with accounts which consumes this API.
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountDomain accountDomain;

    @Autowired
    private GroupDomain groupDomain;

    @Autowired
    private AccountGroupDomain accountGroupDomain;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private PermissionService permissionService;

    //region account

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Account getById(@PathVariable("id") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        AccountObject accountObject = findAccountById(accountId);
        if (!AuthUtils.isMe(accountId)) {
            AuthUtils.checkPermission(accountObject.getOrgId(), "account", "read");
        }
        return new Account(accountObject);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'account:read')")
    public List<Account> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                     @RequestParam(value = "status", required = false) String status,
                                     @RequestParam(value = "search_text", required = false) String searchText,
                                     @RequestParam(value = "start_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime startTime,
                                     @RequestParam(value = "end_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime endTime,
                                     @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                     Pageable pageable,
                                     HttpServletResponse response) {
        orgId = AuthUtils.fixOrgId(orgId); //auto fix current
        Page<AccountObject> accountPage = accountDomain.getByOrgId(orgId, status, searchText, startTime, endTime, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", accountPage.toContentRange());
        }
        return accountPage.map(Account::new).getContent();
    }

    @RequestMapping(value = "count", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'account:read')")
    public Long count(@RequestParam(value = "org_id", required = false) String orgId,
                      @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn) {
        orgId = AuthUtils.fixOrgId(orgId); //auto fix current

        Long count = accountDomain.count(orgId, statusCodeIn);
        return count == null ? 0L : count;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#request.orgId, 'org', 'account:create')")
    public Account create(@RequestBody @Valid AccountRequest request) {
        AccountObject accountObject = request.toAccountObject();
        return new Account(accountDomain.createAccount(accountObject, true));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdateAccount(@PathVariable("id") String accountId,
                                   @RequestBody Account account) {
        accountId = AuthUtils.fixAccountId(accountId);
        AccountObject accountObject = findAccountById(accountId);
        if (!AuthUtils.isMe(accountId)) {
            AuthUtils.checkPermission(accountObject.getOrgId(), "account", "write");
        }
        AccountObject accountObjectNew = new AccountObject();
        accountObjectNew.setId(accountId);
        accountObjectNew.setFirstName(account.getFirstName());
        accountObjectNew.setLastName(account.getLastName());
        accountObjectNew.setEmail(account.getEmail());
        accountObjectNew.setPhone(account.getPhone());
        accountDomain.patchUpdate(accountObjectNew);
    }

    @RequestMapping(value = "{id}/disable", method = RequestMethod.PATCH)
    public void disableAccount(@PathVariable("id") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        AccountObject accountObject = findAccountById(accountId);

        AuthUtils.checkPermission(accountObject.getOrgId(), "account", "write");

        accountDomain.updateStatus(accountId, LookupCodes.AccountStatus.DISABLED);
    }

    @RequestMapping(value = "{id}/enable", method = RequestMethod.PATCH)
    public void enableAccount(@PathVariable("id") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        AccountObject accountObject = findAccountById(accountId);

        AuthUtils.checkPermission(accountObject.getOrgId(), "account", "write");

        accountDomain.updateStatus(accountId, LookupCodes.AccountStatus.AVAILABLE);
    }

    @RequestMapping(value = "{id}/resetPassword", method = RequestMethod.PATCH)
    public void changePassword(@PathVariable("id") String accountId, @RequestBody String newPassword) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        AccountObject accountObject = findAccountById(accountId);

        AuthUtils.checkPermission(accountObject.getOrgId(), "account", "write");

        accountDomain.updatePassword(accountId, newPassword);
    }

    @RequestMapping(value = "current/password", method = RequestMethod.POST)
    public void updatePassword(@RequestBody AccountUpdatePasswordRequest accountPassword) {
        String currentAccountId = AuthUtils.getCurrentAccount().getId();

        AccountObject accountObject = findAccountById(currentAccountId);

        String rawOldPassword = accountPassword.getOldPassword();
        String rawNewPassword = accountPassword.getNewPassword();
        String password = accountObject.getPassword();
        String hashSalt = accountObject.getHashSalt();

        if (!accountDomain.validatePassword(rawOldPassword, hashSalt, password)) {
            throw new UnprocessableEntityException("current password invalid");
        }

        accountDomain.updatePassword(currentAccountId, rawNewPassword);
    }

    //endregion

    //region group

    @RequestMapping(value = "{account_id}/group", method = RequestMethod.GET)
    public List<Group> getGroups(@PathVariable("account_id") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        AccountObject accountObject = findAccountById(accountId);
        if (!AuthUtils.isMe(accountId)) {
            AuthUtils.checkPermission(accountObject.getOrgId(), "account_group", "read");
        }
        return accountGroupDomain.getGroups(accountObject).stream().map(Group::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "{account_id}/group/{group_id}", method = RequestMethod.PUT)
    public void putAccountGroup(@PathVariable(value = "account_id") String accountId,
                                @PathVariable(value = "group_id") String groupId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        AccountObject accountObject = findAccountById(accountId);
        GroupObject groupObject = findGroupById(groupId);
        if (!accountObject.getOrgId().equals(groupObject.getOrgId())) {
            throw new BadRequestException("account and group are not in the same organization");
        }
        AuthUtils.checkPermission(accountObject.getOrgId(), "account_group", "write");
        accountGroupDomain.putAccountGroup(accountId, groupId);
    }

    //update account group under the account
    @RequestMapping(value = "{account_id}/group", method = RequestMethod.PUT)
    public void updateAccountGroups(@PathVariable(value = "account_id") String accountId,
                                    @RequestBody List<String> groupIds) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        AccountObject accountObject = findAccountById(accountId);
        AuthUtils.checkPermission(accountObject.getOrgId(), "account_group", "write");
        accountGroupDomain.putAccountGroupsByAccount(accountObject, groupIds);
    }

    //delete account group under the account
    @RequestMapping(value = "{account_id}/group/{group_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountGroup(@PathVariable(value = "account_id") String accountId,
                                   @PathVariable(value = "group_id") String groupId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        AccountObject accountObject = findAccountById(accountId);
        AuthUtils.checkPermission(accountObject.getOrgId(), "account_group", "write");
        accountGroupDomain.deleteAccountGroupByAccountIdAndGroupId(accountId, groupId);
    }

    //endregion

    //region permission

    /**
     * get all permissions of the account.
     */
    @RequestMapping(value = "{account_id}/permission", method = RequestMethod.GET)
    public List<PermissionEntry> getAllPermissionByAccountId(@PathVariable("account_id") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        if (AuthUtils.isMe(accountId)) {
            return AuthUtils.getAuthentication().getPermissionEntries().stream().map(PermissionEntry::new).collect(Collectors.toList());
        } else {
            AccountObject accountObject = findAccountById(accountId);
            String orgId = accountObject.getOrgId();
            AuthUtils.checkPermission(orgId, "permission_allocation", "read");
            List<com.yunsoo.api.security.permission.PermissionEntry> permissionEntries = permissionService.getExpendedPermissionEntriesByAccountId(accountId);
            //fix orgRestriction
            permissionEntries.forEach(p -> {
                p.setRestriction(authorizationService.fixOrgRestriction(p.getRestriction(), orgId));
            });
            return permissionEntries.stream().map(PermissionEntry::new).collect(Collectors.toList());
        }
    }

    //endregion

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