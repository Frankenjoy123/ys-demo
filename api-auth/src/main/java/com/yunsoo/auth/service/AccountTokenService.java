package com.yunsoo.auth.service;

import com.yunsoo.auth.dao.entity.AccountTokenEntity;
import com.yunsoo.auth.dao.repository.AccountTokenRepository;
import com.yunsoo.auth.dto.Token;
import com.yunsoo.common.util.HashUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by:   Lijian
 * Created on:   2016-07-06
 * Descriptions:
 */
@Service
public class AccountTokenService {

    @Autowired
    private AccountTokenRepository accountTokenRepository;

    /**
     * @param accountId      not null
     * @param appId          not null
     * @param deviceId       nullable
     * @param expiresMinutes nullable
     * @return permanent token object
     */
    public Token createPermanentToken(String accountId, String appId, String deviceId, Integer expiresMinutes) {
        DateTime now = DateTime.now();
        DateTime expires = expiresMinutes != null && expiresMinutes > 0 ? now.plusMinutes(expiresMinutes) : null;
        String permanentToken = HashUtils.sha1HexString(UUID.randomUUID().toString()); //random sha1
        AccountTokenEntity accountTokenEntity = new AccountTokenEntity();
        accountTokenEntity.setAccountId(accountId);
        accountTokenEntity.setAppId(appId);
        accountTokenEntity.setDeviceId(deviceId);
        accountTokenEntity.setPermanentToken(permanentToken);
        accountTokenEntity.setPermanentTokenExpiresDateTime(expires);
        accountTokenEntity.setCreatedDateTime(now);
        accountTokenEntity = accountTokenRepository.save(accountTokenEntity);
        return new Token(accountTokenEntity.getPermanentToken(), accountTokenEntity.getPermanentTokenExpiresDateTime());
    }
}
