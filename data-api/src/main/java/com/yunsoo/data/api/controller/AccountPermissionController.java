package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.AccountPermissionObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.AccountPermissionEntity;
import com.yunsoo.data.service.repository.AccountPermissionRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AccountPermissionObject create(@RequestBody AccountPermissionObject accountPermissionObject) {
        AccountPermissionEntity entity = toAccountPermissionEntity(accountPermissionObject);
        if (entity.getCreatedDatetime() == null) {
            entity.setCreatedDatetime(DateTime.now());
        }
        entity.setId(null);
        return toAccountPermissionObject(accountPermissionRepository.save(entity));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        accountPermissionRepository.delete(id);
    }


    private AccountPermissionObject toAccountPermissionObject(AccountPermissionEntity entity) {
        if (entity == null) {
            return null;
        }
        AccountPermissionObject object = new AccountPermissionObject();
        object.setId(entity.getId());
        object.setAccountId(entity.getAccountId());
        object.setOrgId(entity.getOrgId());
        object.setResourceCode(entity.getResourceCode());
        object.setActionCode(entity.getActionCode());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDatetime(entity.getCreatedDatetime());
        return object;
    }

    private AccountPermissionEntity toAccountPermissionEntity(AccountPermissionObject object) {
        if (object == null) {
            return null;
        }
        AccountPermissionEntity entity = new AccountPermissionEntity();
        entity.setId(object.getId());
        entity.setAccountId(object.getAccountId());
        entity.setOrgId(object.getOrgId());
        entity.setResourceCode(object.getResourceCode());
        entity.setActionCode(object.getActionCode());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDatetime(object.getCreatedDatetime());
        return entity;
    }


}
