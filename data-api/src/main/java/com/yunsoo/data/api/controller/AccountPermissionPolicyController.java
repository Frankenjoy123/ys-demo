package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.AccountPermissionPolicyObject;
import com.yunsoo.data.service.entity.AccountPermissionPolicyEntity;
import com.yunsoo.data.service.repository.AccountPermissionPolicyRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AccountPermissionPolicyObject create(@RequestBody AccountPermissionPolicyObject accountPermissionPolicyObject) {
        AccountPermissionPolicyEntity entity = toAccountPermissionPolicyEntity(accountPermissionPolicyObject);
        entity.setId(null);
        if (entity.getCreatedDatetime() == null) {
            entity.setCreatedDatetime(DateTime.now());
        }
        return toAccountPermissionPolicyObject(accountPermissionPolicyRepository.save(entity));
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam(value = "account_id") String accountId,
                       @RequestParam(value = "org_id") String orgId,
                       @RequestParam(value = "policy_code") String policyCode) {
        accountPermissionPolicyRepository.deleteByAccountIdAndOrgIdAndPolicyCode(accountId, orgId, policyCode);
    }

    private AccountPermissionPolicyObject toAccountPermissionPolicyObject(AccountPermissionPolicyEntity entity) {
        if (entity == null) {
            return null;
        }
        AccountPermissionPolicyObject object = new AccountPermissionPolicyObject();
        object.setId(entity.getId());
        object.setAccountId(entity.getAccountId());
        object.setOrgId(entity.getOrgId());
        object.setPolicyCode(entity.getPolicyCode());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDatetime(entity.getCreatedDatetime());
        return object;
    }

    private AccountPermissionPolicyEntity toAccountPermissionPolicyEntity(AccountPermissionPolicyObject object) {
        if (object == null) {
            return null;
        }
        AccountPermissionPolicyEntity entity = new AccountPermissionPolicyEntity();
        entity.setId(object.getId());
        entity.setAccountId(object.getAccountId());
        entity.setOrgId(object.getOrgId());
        entity.setPolicyCode(object.getPolicyCode());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDatetime(object.getCreatedDatetime());
        return entity;
    }


}
