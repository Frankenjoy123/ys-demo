package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.AccountGroupObject;
import com.yunsoo.data.service.entity.AccountGroupEntity;
import com.yunsoo.data.service.repository.AccountGroupRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/24
 * Descriptions:
 */
@RestController
@RequestMapping("/accountgroup")
public class AccountGroupController {

    @Autowired
    private AccountGroupRepository accountGroupRepository;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<AccountGroupObject> getByFilter(@RequestParam(value = "account_id", required = false) String accountId,
                                                @RequestParam(value = "group_id", required = false) String groupId) {
        List<AccountGroupEntity> entities;
        if (accountId == null) {
            if (groupId == null) {
                entities = accountGroupRepository.findAll();
            } else {
                entities = accountGroupRepository.findByGroupId(groupId);
            }
        } else {
            if (groupId == null) {
                entities = accountGroupRepository.findByAccountId(accountId);
            } else {
                entities = accountGroupRepository.findByAccountIdAndGroupId(accountId, groupId);
            }
        }
        return entities.stream().map(this::toAccountGroupObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AccountGroupObject create(@RequestBody @Valid AccountGroupObject object) {
        AccountGroupEntity entity = toAccountGroupEntity(object);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        AccountGroupEntity newEntity = accountGroupRepository.save(entity);
        return toAccountGroupObject(newEntity);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByAccountIdAndGroupId(@RequestParam(value = "account_id") String accountId,
                                            @RequestParam(value = "group_id") String groupId) {
        accountGroupRepository.deleteByAccountIdAndGroupId(accountId, groupId);
    }


    private AccountGroupObject toAccountGroupObject(AccountGroupEntity entity) {
        if (entity == null) {
            return null;
        }
        AccountGroupObject object = new AccountGroupObject();
        object.setAccountId(entity.getAccountId());
        object.setGroupId(entity.getGroupId());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        return object;
    }

    private AccountGroupEntity toAccountGroupEntity(AccountGroupObject object) {
        if (object == null) {
            return null;
        }
        AccountGroupEntity entity = new AccountGroupEntity();
        entity.setAccountId(object.getAccountId());
        entity.setGroupId(object.getGroupId());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }

}
