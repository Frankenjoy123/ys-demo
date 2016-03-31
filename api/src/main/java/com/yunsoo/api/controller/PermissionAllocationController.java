package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.PermissionAllocationDomain;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.PermissionAllocationObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-03-31
 * Descriptions:
 */
@RestController
@RequestMapping("/permissionAllocation")
public class PermissionAllocationController {

    @Autowired
    private PermissionAllocationDomain permissionAllocationDomain;

    @Autowired
    private AccountDomain accountDomain;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<PermissionAllocationObject> getByFiller(@RequestParam(value = "account_id") String accountId) {
        accountId = AuthUtils.fixAccountId(accountId);
        if (!AuthUtils.isMe(accountId)) {
            AccountObject accountObject = accountDomain.getById(accountId);
            AuthUtils.checkPermission(AuthUtils.getCurrentAccount().getOrgId(), "permission_allocation:read");
        }
        return permissionAllocationDomain.getAllPermissionAllocationsByAccountId(accountId);
    }


}
