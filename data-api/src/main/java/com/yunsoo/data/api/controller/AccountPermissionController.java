package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.AccountPermissionObject;
import com.yunsoo.data.service.entity.AccountPermissionEntity;
import com.yunsoo.data.service.repository.AccountPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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


    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<AccountPermissionObject> getPermissionsByAccountId(@RequestParam(value = "account_id") String accountId) {
        return accountPermissionRepository.findByAccountId(accountId).stream()
                .map(this::toAccountPermissionObject).collect(Collectors.toList());
    }


    private AccountPermissionObject toAccountPermissionObject(AccountPermissionEntity entity) {
        if (entity == null) {
            return null;
        }
        AccountPermissionObject object = new AccountPermissionObject();
        object.setAccountId(entity.getAccountId());
        object.setOrgId(entity.getOrgId());
        object.setResourceCode(entity.getResourceCode());
        object.setActionCode(entity.getActionCode());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDatetime(entity.getCreatedDatetime());
        return object;
    }

}
