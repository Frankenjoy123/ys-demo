package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.AccountPermissionDomain;
import com.yunsoo.api.dto.AccountPermission;
import com.yunsoo.api.dto.AccountPermissionPolicy;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/17
 * Descriptions:
 */
@RestController
@RequestMapping("/account")
public class AccountPermissionController {

    @Autowired
    private AccountDomain accountDomain;
    @Autowired
    private AccountPermissionDomain accountPermissionDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "{account_id}/permission/account", method = RequestMethod.GET)
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

    @RequestMapping(value = "{account_id}/permission", method = RequestMethod.GET)
    public List<AccountPermission> getByAccountId(@PathVariable("account_id") String accountId) {
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
            accountPermissionDomain.checkPermission(new TPermission(accountObject.getOrgId(), "accountpermission", "read"));
        }
    }

}
