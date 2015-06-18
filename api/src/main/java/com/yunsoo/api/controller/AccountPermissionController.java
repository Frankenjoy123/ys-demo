package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountPermissionDomain;
import com.yunsoo.api.dto.AccountPermission;
import com.yunsoo.api.dto.AccountPermissionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/17
 * Descriptions:
 */
@RestController
@RequestMapping("/account")
public class AccountPermissionController {

    @Autowired
    private AccountPermissionDomain accountPermissionDomain;

    @RequestMapping(value = "{accountId}/permission", method = RequestMethod.GET)
    public List<AccountPermission> getPermissionsByAccountId(@PathVariable(value = "accountId") String accountId) {
        return accountPermissionDomain.getAccountPermissions(accountId);
    }

    @RequestMapping(value = "{accountId}/permissionpolicy", method = RequestMethod.GET)
    public List<AccountPermissionPolicy> getPermissionPoliciesByAccountId(@PathVariable(value = "accountId") String accountId) {
        return accountPermissionDomain.getAccountPermissionPolicies(accountId);
    }

    @RequestMapping(value = "{accountId}/permission/page", method = RequestMethod.GET)
    public List<AccountPermission> getByAccountId(@PathVariable("accountId") String accountId) {


        return new ArrayList<>();
    }

}
