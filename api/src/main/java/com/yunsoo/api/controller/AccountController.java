package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.AccountGroupDomain;
import com.yunsoo.api.domain.AccountPermissionDomain;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
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
    private AccountPermissionDomain accountPermissionDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Account getById(@PathVariable("id") String accountId) {
        if ("current".equals(accountId)) { //get current Account
            accountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        }
        AccountObject accountObject = findAccountById(accountId);
        return new Account(accountObject);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'filterByOrg', 'account:read')")
    public List<Account> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                     @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                     Pageable pageable,
                                     HttpServletResponse response) {
        if (orgId == null) {
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }

        Page<AccountObject> accountPage = accountDomain.getByOrgId(orgId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", accountPage.toContentRange());
        }
        return accountPage.map(Account::new).getContent();
    }

    @RequestMapping(value = "count", method = RequestMethod.GET)
    public Long count(@RequestParam(value = "org_id", required = false) String orgId,
                      @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn) {
        if (orgId == null) {
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }

        Long count = accountDomain.count(orgId, statusCodeIn);
        return count == null ? 0L : count;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#request.orgId, 'filterByOrg', 'account:create')")
    public Account create(@Valid @RequestBody AccountRequest request) {
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        AccountObject accountObject = toAccountObject(request);
        accountObject.setCreatedAccountId(currentAccountId);

        return new Account(accountDomain.createAccount(accountObject));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void updateStatus(@PathVariable("id") String accountId, @RequestBody String statusCode) {
        //todo
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable("id") String accountId, @RequestBody AccountRequest accountRequest) {
        //todo
    }

    @RequestMapping(value = "current/password", method = RequestMethod.POST)
    public void updatePassword(@RequestBody AccountUpdatePasswordRequest accountPassword) {

        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();

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


    //groups

    @RequestMapping(value = "{account_id}/group", method = RequestMethod.GET)
    public List<Group> getGroups(@PathVariable("account_id") String accountId) {
        if ("current".equals(accountId)) { //get current Account
            accountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        }
        AccountObject accountObject = findAccountById(accountId);
        return accountGroupDomain.getGroups(accountObject).stream().map(Group::new).collect(Collectors.toList());
    }

    //create account group under the account
    @RequestMapping(value = "{account_id}/group", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createAccountGroup(@PathVariable(value = "account_id") String accountId,
                                     @RequestBody String groupId) {
        TAccount currentAccount = tokenAuthenticationService.getAuthentication().getDetails();
        AccountGroupObject exists = accountGroupDomain.getAccountGroupByAccountIdAndGroupId(accountId, groupId);
        if (exists != null) {
            throw new ConflictException("account id: " + accountId + "group id: " + groupId + "already exist.");
        }
        AccountGroupObject accountGroupObject = new AccountGroupObject();
        accountGroupObject.setAccountId(accountId);
        accountGroupObject.setGroupId(groupId);
        accountGroupObject.setCreatedAccountId(currentAccount.getId());
        accountGroupObject.setCreatedDateTime(DateTime.now());
        accountGroupDomain.createAccountGroup(accountGroupObject);
        return groupId;
    }

    //delete account group under the account
    @RequestMapping(value = "{account_id}/group", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountGroup(@PathVariable(value = "account_id") String accountId,
                                   @RequestBody String groupId) {
        accountGroupDomain.deleteAccountGroup(groupId, accountId);
    }

    //update account group under the account
    @RequestMapping(value = "{account_id}/group", method = RequestMethod.PUT)
    public void updateAccountGroup(@PathVariable(value = "account_id") String accountId,
                                   @RequestBody @Valid List<String> groupIds) {
        List<AccountGroupObject> accountGroupObjects = accountGroupDomain.getAccountGroupByAccountId(accountId);
        TAccount currentAccount = tokenAuthenticationService.getAuthentication().getDetails();
        List<String> originalGroupId = new ArrayList<>();
        if (accountGroupObjects == null) {
            throw new NotFoundException("account group not found");
        }
        for (AccountGroupObject ago : accountGroupObjects) {
            // ago not in group ids
            if (!groupIds.contains(ago.getGroupId())) {
                accountGroupDomain.deleteAccountGroup(ago.getGroupId(), ago.getAccountId());
            }
            originalGroupId.add(ago.getGroupId());
        }
        // gid not in accountGroupObjects
        groupIds.stream().filter(gid -> !originalGroupId.contains(gid)).forEach(gid -> {
            AccountGroupObject accountGroupObject = new AccountGroupObject();
            accountGroupObject.setAccountId(accountId);
            accountGroupObject.setGroupId(gid);
            accountGroupObject.setCreatedAccountId(currentAccount.getId());
            accountGroupObject.setCreatedDateTime(DateTime.now());
            accountGroupDomain.createAccountGroup(accountGroupObject);
        });
    }

    //permissions

    /**
     * get the permissions directly related to the account
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = "{account_id}/permission", method = RequestMethod.GET)
    public List<AccountPermission> getPermissionsByAccountId(@PathVariable(value = "account_id") String accountId) {
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        if ("current".equals(accountId)) { //get current Account
            accountId = currentAccountId;
        }
        checkAccountPermissionRead(currentAccountId, accountId);
        return accountPermissionDomain.getAccountPermissions(accountId)
                .stream()
                .map(AccountPermission::new)
                .collect(Collectors.toList());
    }

    /**
     * get the permission policies related to the account
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = "{account_id}/permission/policy", method = RequestMethod.GET)
    public List<AccountPermissionPolicy> getPermissionPoliciesByAccountId(@PathVariable(value = "account_id") String accountId) {
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        if ("current".equals(accountId)) { //get current Account
            accountId = currentAccountId;
        }
        checkAccountPermissionRead(currentAccountId, accountId);
        return accountPermissionDomain.getAccountPermissionPolicies(accountId)
                .stream()
                .map(AccountPermissionPolicy::new)
                .collect(Collectors.toList());
    }

    /**
     * get all the permissions related to the account, include group permissions and permission policies.
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = "{account_id}/allpermission", method = RequestMethod.GET)
    public List<AccountPermission> getAllPermissionByAccountId(@PathVariable("account_id") String accountId) {
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        if ("current".equals(accountId)) { //get current Account
            accountId = currentAccountId;
        }
        checkAccountPermissionRead(currentAccountId, accountId);
        return accountPermissionDomain.getAllAccountPermissions(accountId)
                .stream()
                .map(AccountPermission::new)
                .collect(Collectors.toList());
    }

    private void checkAccountPermissionRead(String currentAccountId, String accountId) {
        if (!currentAccountId.equals(accountId)) {
            //check permission
            AccountObject accountObject = accountDomain.getById(accountId);
            if (accountObject == null) {
                throw new NotFoundException("account not found by id [" + accountId + "]");
            }
            accountPermissionDomain.checkPermission(currentAccountId, new TPermission(accountObject.getOrgId(), "accountpermission", "read"));
        }
    }


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