package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.AccountLoginLogObject;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.AccountLoginLogEntity;
import com.yunsoo.data.service.repository.AccountLoginLogRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-05-03
 * Descriptions:
 */
@RestController
@RequestMapping("/accountLoginLog")
public class AccountLoginLogController {

    @Autowired
    private AccountLoginLogRepository accountLoginLogRepository;


    @RequestMapping(value = "", method = RequestMethod.POST)
    public AccountLoginLogObject saveLog(@RequestBody @Valid AccountLoginLogObject obj) {
        obj.setId(null);
        if (obj.getCreatedDateTime() == null) {
            obj.setCreatedDateTime(DateTime.now());
        }
        AccountLoginLogEntity entity = accountLoginLogRepository.save(toAccountLoginLogEntity(obj));
        return toAccountLoginLogObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<AccountLoginLogObject> getByFilter(@RequestParam(value = "account_id", required = true) String accountId,
                                                   Pageable pageable,
                                                   HttpServletResponse response) {

        Page<AccountLoginLogEntity> entityPage = accountLoginLogRepository.findByAccountId(accountId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }

        return entityPage.getContent().stream().map(this::toAccountLoginLogObject).collect(Collectors.toList());
    }


    private AccountLoginLogEntity toAccountLoginLogEntity(AccountLoginLogObject obj) {
        if (obj == null) {
            return null;
        }
        AccountLoginLogEntity entity = new AccountLoginLogEntity();
        entity.setId(obj.getId());
        entity.setAccountId(obj.getAccountId());
        entity.setAppId(obj.getAppId());
        entity.setChannel(obj.getChannel());
        entity.setDeviceId(obj.getDeviceId());
        entity.setIp(obj.getIp());
        entity.setUserAgent(obj.getUserAgent());
        entity.setCreatedDateTime(obj.getCreatedDateTime());
        return entity;
    }

    private AccountLoginLogObject toAccountLoginLogObject(AccountLoginLogEntity entity) {
        if (entity == null) {
            return null;
        }
        AccountLoginLogObject obj = new AccountLoginLogObject();
        obj.setId(entity.getId());
        obj.setAccountId(entity.getAccountId());
        obj.setAppId(entity.getAppId());
        obj.setChannel(entity.getChannel());
        obj.setDeviceId(entity.getDeviceId());
        obj.setIp(entity.getIp());
        obj.setUserAgent(entity.getUserAgent());
        obj.setCreatedDateTime(entity.getCreatedDateTime());
        return obj;
    }

}
