package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.AccountGroupDomain;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.security.authorization.AuthorizationService;
import com.yunsoo.api.security.permission.PermissionService;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.AccountGroupObject;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
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
    private AccountGroupDomain accountGroupDomain;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private PermissionService permissionService;

    //region account

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'account:read')")
    public Account getById(@PathVariable("id") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current

        AccountObject accountObject = findAccountById(accountId);
        return new Account(accountObject);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'account:read')")
    public List<Account> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                     @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                     Pageable pageable,
                                     HttpServletResponse response) {
        orgId = AuthUtils.fixOrgId(orgId); //auto fix current

        Page<AccountObject> accountPage = accountDomain.getByOrgId(orgId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", accountPage.toContentRange());
        }
        return accountPage.map(Account::new).getContent();
    }

    @RequestMapping(value = "count", method = RequestMethod.GET)
    public Long count(@RequestParam(value = "org_id", required = false) String orgId,
                      @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn) {
        orgId = AuthUtils.fixOrgId(orgId); //auto fix current

        Long count = accountDomain.count(orgId, statusCodeIn);
        return count == null ? 0L : count;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#request.orgId, 'org', 'account:create')")
    public Account create(@Valid @RequestBody AccountRequest request) {
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        AccountObject accountObject = toAccountObject(request);
        accountObject.setCreatedAccountId(currentAccountId);

        return new Account(accountDomain.createAccount(accountObject, true));
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

    @RequestMapping(value = "{id}/resetpassword", method = RequestMethod.PATCH)
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
            throw new UnprocessableEntityException("当前密码不匹配");
        }

        accountDomain.updatePassword(currentAccountId, rawNewPassword);
    }

    //endregion

    //region group

    @RequestMapping(value = "{account_id}/group", method = RequestMethod.GET)
    public List<Group> getGroups(@PathVariable("account_id") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        findAccountById(accountId);
        AccountObject accountObject = findAccountById(accountId);
        return accountGroupDomain.getGroups(accountObject).stream().map(Group::new).collect(Collectors.toList());
    }

    //create account group under the account
    @RequestMapping(value = "{account_id}/group/{group_id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createAccountGroup(@PathVariable(value = "account_id") String accountId,
                                     @PathVariable(value = "group_id") String groupId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        findAccountById(accountId);
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
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
        return groupId;
    }

    //delete account group under the account
    @RequestMapping(value = "{account_id}/group/{group_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountGroup(@PathVariable(value = "account_id") String accountId,
                                   @PathVariable(value = "group_id") String groupId) {
        accountId = AuthUtils.fixAccountId(accountId); //auto fix current
        findAccountById(accountId);
        accountGroupDomain.deleteAccountGroupByAccountIdAndGroupId(accountId, groupId);
    }

    //update account group under the account
    @RequestMapping(value = "{account_id}/group", method = RequestMethod.PUT)
    public void updateAccountGroup(@PathVariable(value = "account_id") String accountId,
                                   @RequestBody @Valid List<String> groupIds) {
        findAccountById(accountId);
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        List<String> originalGroupIds = accountGroupDomain.getAccountGroupByAccountId(accountId).stream()
                .map(AccountGroupObject::getGroupId)
                .collect(Collectors.toList());
        //delete original but not in the new groupId list
        originalGroupIds.stream().filter(gId -> !groupIds.contains(gId)).forEach(gId -> {
            accountGroupDomain.deleteAccountGroupByAccountIdAndGroupId(accountId, gId);
        });
        //add not in the original groupId list
        groupIds.stream().filter(gId -> !originalGroupIds.contains(gId)).forEach(gId -> {
            AccountGroupObject accountGroupObject = new AccountGroupObject();
            accountGroupObject.setAccountId(accountId);
            accountGroupObject.setGroupId(gId);
            accountGroupObject.setCreatedAccountId(currentAccountId);
            accountGroupObject.setCreatedDateTime(DateTime.now());
            accountGroupDomain.createAccountGroup(accountGroupObject);
        });
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

    private AccountObject toAccountObject(AccountRequest request) {
        if (request == null) {
            return null;
        }
        AccountObject accountObject = new AccountObject();
        accountObject.setOrgId(request.getOrgId());
        accountObject.setIdentifier(request.getIdentifier());
        accountObject.setEmail(request.getEmail());
        accountObject.setFirstName(request.getFirstName());
        accountObject.setLastName(request.getLastName());
        accountObject.setPhone(request.getPhone());
        accountObject.setPassword(request.getPassword());
        return accountObject;
    }

}