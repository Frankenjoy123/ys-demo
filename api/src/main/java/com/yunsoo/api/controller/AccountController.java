package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.AccountGroupDomain;
import com.yunsoo.api.domain.AccountPermissionDomain;
import com.yunsoo.api.domain.PermissionDomain;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.AccountGroupObject;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.AccountPermissionObject;
import com.yunsoo.common.data.object.AccountPermissionPolicyObject;
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
import org.springframework.util.StringUtils;
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
    private AccountPermissionDomain accountPermissionDomain;

    @Autowired
    private PermissionDomain permissionDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    //region account

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Account getById(@PathVariable("id") String accountId) {
        accountId = fixAccountId(accountId); //auto fix current

        AccountObject accountObject = findAccountById(accountId);
        return new Account(accountObject);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'filterByOrg', 'account:read')")
    public List<Account> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                     @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                     Pageable pageable,
                                     HttpServletResponse response) {
        orgId = fixOrgId(orgId); //auto fix current

        Page<AccountObject> accountPage = accountDomain.getByOrgId(orgId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", accountPage.toContentRange());
        }
        return accountPage.map(Account::new).getContent();
    }

    @RequestMapping(value = "count", method = RequestMethod.GET)
    public Long count(@RequestParam(value = "org_id", required = false) String orgId,
                      @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn) {
        orgId = fixOrgId(orgId); //auto fix current

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
        accountId = fixAccountId(accountId); //auto fix current
        findAccountById(accountId);
        //todo
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable("id") String accountId, @RequestBody AccountRequest accountRequest) {
        accountId = fixAccountId(accountId); //auto fix current
        findAccountById(accountId);
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

    //endregion

    //region group

    @RequestMapping(value = "{account_id}/group", method = RequestMethod.GET)
    public List<Group> getGroups(@PathVariable("account_id") String accountId) {
        accountId = fixAccountId(accountId); //auto fix current
        findAccountById(accountId);
        AccountObject accountObject = findAccountById(accountId);
        return accountGroupDomain.getGroups(accountObject).stream().map(Group::new).collect(Collectors.toList());
    }

    //create account group under the account
    @RequestMapping(value = "{account_id}/group/{group_id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createAccountGroup(@PathVariable(value = "account_id") String accountId,
                                     @PathVariable(value = "group_id") String groupId) {
        accountId = fixAccountId(accountId); //auto fix current
        findAccountById(accountId);
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
        return groupId;
    }

    //delete account group under the account
    @RequestMapping(value = "{account_id}/group/{group_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountGroup(@PathVariable(value = "account_id") String accountId,
                                   @PathVariable(value = "group_id") String groupId) {
        accountId = fixAccountId(accountId); //auto fix current
        findAccountById(accountId);
        accountGroupDomain.deleteAccountGroupByAccountIdAndGroupId(accountId, groupId);
    }

    //update account group under the account
    @RequestMapping(value = "{account_id}/group", method = RequestMethod.PUT)
    public void updateAccountGroup(@PathVariable(value = "account_id") String accountId,
                                   @RequestBody @Valid List<String> groupIds) {
        findAccountById(accountId);
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
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

    //region accountpermission

    /**
     * get the permissions directly related to the account
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = "{account_id}/accountpermission", method = RequestMethod.GET)
    public List<AccountPermission> getPermissionsByAccountId(@PathVariable(value = "account_id") String accountId) {
        accountId = fixAccountId(accountId); //auto fix current
        findAccountById(accountId);
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        checkAccountPermissionRead(currentAccountId, accountId);
        return accountPermissionDomain.getAccountPermissions(accountId)
                .stream()
                .map(AccountPermission::new)
                .collect(Collectors.toList());
    }

    //create account permission
    @RequestMapping(value = "{account_id}/accountpermission", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AccountPermission createAccountPermission(@PathVariable(value = "account_id") String accountId,
                                                     @RequestBody @Valid AccountPermission accountPermission) {
        accountId = fixAccountId(accountId); //auto fix current
        findAccountById(accountId);
        AccountPermissionObject accountPermissionObject = accountPermission.toAccountPermissionObject();
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        accountPermissionObject.setOrgId(fixOrgId(accountPermissionObject.getOrgId()));
        accountPermissionObject.setAccountId(accountId);
        accountPermissionObject.setCreatedAccountId(currentAccountId);
        accountPermissionObject.setCreatedDatetime(DateTime.now());
        accountPermissionObject = accountPermissionDomain.createAccountPermission(accountPermissionObject);
        return new AccountPermission(accountPermissionObject);
    }

    //delete account permission
    @RequestMapping(value = "{account_id}/accountpermission/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountPermission(@PathVariable(value = "account_id") String accountId,
                                        @PathVariable("id") String id) {
        accountId = fixAccountId(accountId); //auto fix current
        findAccountById(accountId);
        accountPermissionDomain.deleteAccountPermissionById(id);
    }

    //endregion

    //region accountpermissionpolicy

    /**
     * get the permission policies related to the account
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = "{account_id}/accountpermissionpolicy", method = RequestMethod.GET)
    public List<AccountPermissionPolicy> getPermissionPoliciesByAccountId(@PathVariable(value = "account_id") String accountId) {
        accountId = fixAccountId(accountId); //auto fix current
        findAccountById(accountId);
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        checkAccountPermissionRead(currentAccountId, accountId);
        return accountPermissionDomain.getAccountPermissionPolicies(accountId)
                .stream()
                .map(AccountPermissionPolicy::new)
                .collect(Collectors.toList());
    }

    //create account permission policy
    @RequestMapping(value = "{account_id}/accountpermissionpolicy", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AccountPermissionPolicy createAccountPermissionPolicy(@PathVariable(value = "account_id") String accountId,
                                                                 @RequestBody @Valid AccountPermissionPolicy accountPermissionPolicy) {
        accountId = fixAccountId(accountId); //auto fix current
        findAccountById(accountId);
        AccountPermissionPolicyObject accountPermissionPolicyObject = accountPermissionPolicy.toAccountPermissionPolicyObject();
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        accountPermissionPolicyObject.setAccountId(accountId);
        accountPermissionPolicyObject.setOrgId(fixOrgId(accountPermissionPolicyObject.getOrgId()));
        accountPermissionPolicyObject.setCreatedAccountId(currentAccountId);
        accountPermissionPolicyObject.setCreatedDatetime(DateTime.now());
        accountPermissionPolicyObject = accountPermissionDomain.createAccountPermissionPolicy(accountPermissionPolicyObject);
        return new AccountPermissionPolicy(accountPermissionPolicyObject);
    }

    //delete account permission policy
    @RequestMapping(value = "{account_id}/accountpermissionpolicy", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountPermissionPolicy(@PathVariable(value = "account_id") String accountId,
                                              @RequestParam(value = "org_id", required = false) String orgId,
                                              @RequestParam(value = "policy_code") String policyCode) {
        accountId = fixAccountId(accountId); //auto fix current
        orgId = fixOrgId(orgId);
        findAccountById(accountId);
        accountPermissionDomain.deleteAccountPermissionPolicy(accountId, orgId, policyCode);
    }

    //endregion

    //region permission

    /**
     * get all the permissions related to the account, include group permissions and permission policies.
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = "{account_id}/permission", method = RequestMethod.GET)
    public List<PermissionInstance> getAllPermissionByAccountId(@PathVariable("account_id") String accountId,
                                                                @RequestParam(value = "org_id", required = false) String orgId) {
        accountId = fixAccountId(accountId); //auto fix current
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        checkAccountPermissionRead(currentAccountId, accountId);
        List<PermissionInstance> permissionInstances = accountPermissionDomain.getPermissionsByAccountId(accountId);
        if (!StringUtils.isEmpty(orgId)) {
            permissionInstances = accountPermissionDomain.filterPermissionsByOrgId(permissionInstances, fixOrgId(orgId));
        }
        return permissionDomain.extendPermissions(permissionInstances);
    }

    //endregion

    private String fixAccountId(String accountId) {
        if (accountId == null || "current".equals(accountId)) {
            //current accountId
            return tokenAuthenticationService.getAuthentication().getDetails().getId();
        }
        return accountId;
    }

    private String fixOrgId(String orgId) {
        if (orgId == null || "current".equals(orgId)) {
            //current orgId
            return tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return orgId;
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