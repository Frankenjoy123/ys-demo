package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.AccountTokenObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.AccountTokenEntity;
import com.yunsoo.data.service.repository.AccountTokenRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Jerry
 * Created on  : 2015/3/15
 * Descriptions:
 * Modified by : Lijian
 * Modified on : 2015/4/20
 */
@RestController
@RequestMapping("/accountToken")
public class AccountTokenController {

    @Autowired
    private AccountTokenRepository accountTokenRepository;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public AccountTokenObject getById(@PathVariable(value = "id") String id) {
        AccountTokenEntity entity = accountTokenRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("AccountToken not found by [id: " + id + "]");
        }
        return toAccountTokenObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<AccountTokenObject> getByFilter(@RequestParam(value = "permanent_token", required = false) String permanentToken,
                                                @RequestParam(value = "device_id", required = false) String deviceId) {
        List<AccountTokenEntity> entities;
        if (StringUtils.hasText(permanentToken)) {
            entities = accountTokenRepository.findByPermanentToken(permanentToken);
        } else if (StringUtils.hasText(deviceId)) {
            entities = accountTokenRepository.findByDeviceId(deviceId);
        } else {
            throw new BadRequestException("filter parameters are missing");
        }
        return entities.stream().map(this::toAccountTokenObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public AccountTokenObject create(@RequestBody AccountTokenObject accountToken) {
        if (StringUtils.isEmpty(accountToken.getAccountId())
                || StringUtils.isEmpty(accountToken.getAppId())
                || StringUtils.isEmpty(accountToken.getPermanentToken())) {
            throw new BadRequestException("accountId, appId or permanentToken must not be null or empty");
        }
        if (accountToken.getCreatedDateTime() == null) {
            accountToken.setCreatedDateTime(DateTime.now());
        }
        accountToken.setId(null); //make sure it's create
        AccountTokenEntity entity = toAccountTokenEntity(accountToken);
        AccountTokenEntity newEntity = accountTokenRepository.save(entity);
        return toAccountTokenObject(newEntity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable(value = "id") String id, @RequestBody AccountTokenObject accountToken) {
        AccountTokenEntity entity = accountTokenRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("AccountToken not found by [id: " + id + "]");
        }
        if (accountToken.getPermanentTokenExpiresDateTime() != null) {
            entity.setPermanentTokenExpiresDateTime(accountToken.getPermanentTokenExpiresDateTime());
        }
        accountTokenRepository.save(entity);
    }


    private AccountTokenObject toAccountTokenObject(AccountTokenEntity entity) {
        if (entity == null) {
            return null;
        }
        AccountTokenObject object = new AccountTokenObject();
        object.setId(entity.getId());
        object.setAccountId(entity.getAccountId());
        object.setAppId(entity.getAppId());
        object.setDeviceId(entity.getDeviceId());
        object.setPermanentToken(entity.getPermanentToken());
        object.setPermanentTokenExpiresDateTime(entity.getPermanentTokenExpiresDateTime());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        return object;
    }

    private AccountTokenEntity toAccountTokenEntity(AccountTokenObject object) {
        if (object == null) {
            return null;
        }
        AccountTokenEntity entity = new AccountTokenEntity();
        entity.setId(object.getId());
        entity.setAccountId(object.getAccountId());
        entity.setAppId(object.getAppId());
        entity.setDeviceId(object.getDeviceId());
        entity.setPermanentToken(object.getPermanentToken());
        entity.setPermanentTokenExpiresDateTime(object.getPermanentTokenExpiresDateTime());
        entity.setCreatedAccountId(object.getAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }
}
