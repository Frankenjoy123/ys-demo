package com.yunsoo.auth.service;

import com.yunsoo.auth.api.util.PageUtils;
import com.yunsoo.auth.dao.entity.AccountLoginLogEntity;
import com.yunsoo.auth.dao.repository.AccountLoginLogRepository;
import com.yunsoo.auth.dto.AccountLoginLog;
import com.yunsoo.common.web.client.Page;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by:   Lijian
 * Created on:   2016-07-26
 * Descriptions:
 */
@Service
public class AccountLoginLogService {

    private static final String CHANNEL_PASSWORD = "password";
    private static final String CHANNEL_TOKEN = "token";

    @Autowired
    private AccountLoginLogRepository accountLoginLogRepository;


    public Page<AccountLoginLog> getByAccountId(String accountId, Pageable pageable) {
        return PageUtils.convert(accountLoginLogRepository.findByAccountId(accountId, pageable)).map(this::toAccountLoginLog);
    }

    public void savePasswordLogin(String accountId,
                                  String appId,
                                  String deviceId,
                                  String ip,
                                  String userAgent) {
        AccountLoginLogEntity entity = new AccountLoginLogEntity();
        entity.setAccountId(accountId);
        entity.setAppId(appId);
        entity.setChannel(CHANNEL_PASSWORD);
        entity.setDeviceId(deviceId);
        entity.setIp(ip);
        entity.setUserAgent(userAgent);
        entity.setCreatedDateTime(DateTime.now());

        saveLog(entity);
    }

    public void saveTokenLogin(String accountId,
                               String appId,
                               String deviceId,
                               String ip,
                               String userAgent) {
        AccountLoginLogEntity entity = new AccountLoginLogEntity();
        entity.setAccountId(accountId);
        entity.setAppId(appId);
        entity.setChannel(CHANNEL_TOKEN);
        entity.setDeviceId(deviceId);
        entity.setIp(ip);
        entity.setUserAgent(userAgent);
        entity.setCreatedDateTime(DateTime.now());

        saveLog(entity);
    }

    private void saveLog(AccountLoginLogEntity entity) {
        entity.setId(null);
        entity.setCreatedDateTime(DateTime.now());
        accountLoginLogRepository.save(entity);
    }

    private AccountLoginLog toAccountLoginLog(AccountLoginLogEntity entity) {
        if (entity == null) {
            return null;
        }
        AccountLoginLog accountLoginLog = new AccountLoginLog();
        accountLoginLog.setId(entity.getId());
        accountLoginLog.setAccountId(entity.getId());
        accountLoginLog.setChannel(entity.getChannel());
        accountLoginLog.setAppId(entity.getAppId());
        return accountLoginLog;
    }

}
