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
import java.util.stream.StreamSupport;

/**
 * Created by:   Lijian
 * Created on:   2015/4/13
 * Descriptions:
 */
@RestController
@RequestMapping("/accountpermission")
public class AccountPermissionController {

    @Autowired
    private AccountPermissionRepository accountPermissionRepository;

    @Autowired
    private AccountPermissionPolicyRepository accountPermissionPolicyRepository;

    @RequestMapping(value = "permission/{accountId}", method = RequestMethod.GET)
    public List<AccountPermissionObject> getPermissionsByAccountId(@PathVariable(value = "accountId") long accountId) {
        return StreamSupport.stream(accountPermissionRepository.findByAccountId(accountId).spliterator(), false)
                .map(pe -> {
                    AccountPermissionObject p = new AccountPermissionObject();
                    p.setOrgId(pe.getOrgId());
                    p.setResourceCode(pe.getResourceCode());
                    p.setActionCode(pe.getActionCode());
                    return p;
                }).collect(Collectors.toList());
    }

    @RequestMapping(value = "permissionpolicy/{accountId}", method = RequestMethod.GET)
    public List<AccountPermissionPolicyObject> getPermissionPoliciesByAccountId(@PathVariable(value = "accountId") long accountId) {
        return StreamSupport.stream(accountPermissionPolicyRepository.findByAccountId(accountId).spliterator(), false)
                .map(pe -> {
                    AccountPermissionPolicyObject pp = new AccountPermissionPolicyObject();
                    pp.setOrgId(pe.getOrgId());
                    pp.setPolicyCode(pe.getPolicyCode());
                    return pp;
                }).collect(Collectors.toList());
    }

}
