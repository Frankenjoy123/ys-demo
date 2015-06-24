package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.AccountPermissionPolicyObject;
import com.yunsoo.data.service.entity.AccountPermissionPolicyEntity;
import com.yunsoo.data.service.repository.AccountPermissionPolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/24
 * Descriptions:
 */
@RestController
@RequestMapping("/accountpermissionpolicy")
public class AccountPermissionPolicyController {

    @Autowired
    private AccountPermissionPolicyRepository accountPermissionPolicyRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<AccountPermissionPolicyObject> getPermissionPoliciesByAccountId(@RequestParam(value = "account_id") String accountId) {
        return accountPermissionPolicyRepository.findByAccountId(accountId).stream()
                .map(this::toAccountPermissionPolicyObject).collect(Collectors.toList());
    }

    private AccountPermissionPolicyObject toAccountPermissionPolicyObject(AccountPermissionPolicyEntity entity) {
        if (entity == null) {
            return null;
        }
        AccountPermissionPolicyObject object = new AccountPermissionPolicyObject();
        object.setAccountId(entity.getAccountId());
        object.setOrgId(entity.getOrgId());
        object.setPolicyCode(entity.getPolicyCode());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDatetime(entity.getCreatedDatetime());
        return object;
    }

}
