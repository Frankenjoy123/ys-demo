package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.AccountPermissionObject;
import com.yunsoo.common.data.object.AccountPermissionPolicyObject;
import com.yunsoo.data.service.repository.AccountPermissionPolicyRepository;
import com.yunsoo.data.service.repository.AccountPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/4/13
 * Descriptions:
 */
@RestController
public class AccountPermissionController {

    @Autowired
    private AccountPermissionRepository accountPermissionRepository;

    @Autowired
    private AccountPermissionPolicyRepository accountPermissionPolicyRepository;

    @RequestMapping(value = "accountpermission/{accountId}", method = RequestMethod.GET)
    public List<AccountPermissionObject> getPermissionsByAccountId(@PathVariable(value = "accountId") String accountId) {
        return accountPermissionRepository.findByAccountId(accountId).stream()
                .map(pe -> {
                    AccountPermissionObject p = new AccountPermissionObject();
                    p.setAccountId(pe.getAccountId());
                    p.setOrgId(pe.getOrgId());
                    p.setResourceCode(pe.getResourceCode());
                    p.setActionCode(pe.getActionCode());
                    return p;
                }).collect(Collectors.toList());
    }

    @RequestMapping(value = "accountpermissionpolicy/{accountId}", method = RequestMethod.GET)
    public List<AccountPermissionPolicyObject> getPermissionPoliciesByAccountId(@PathVariable(value = "accountId") String accountId) {
        return accountPermissionPolicyRepository.findByAccountId(accountId).stream()
                .map(ppe -> {
                    AccountPermissionPolicyObject pp = new AccountPermissionPolicyObject();
                    pp.setAccountId(ppe.getAccountId());
                    pp.setOrgId(ppe.getOrgId());
                    pp.setPolicyCode(ppe.getPolicyCode());
                    return pp;
                }).collect(Collectors.toList());
    }

}
