package com.yunsoo.auth.service;

import com.yunsoo.auth.dao.entity.AccountTokenEntity;
import com.yunsoo.auth.dao.repository.AccountTokenRepository;
import com.yunsoo.auth.dto.AccountToken;
import com.yunsoo.auth.dto.Application;
import com.yunsoo.auth.dto.Token;
import com.yunsoo.common.util.HashUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-07-06
 * Descriptions:
 */
@Service
public class AccountTokenService {

    private static final int PERMANENT_TOKEN_EXPIRES_MINUTES = 4000;

    @Autowired
    private AccountTokenRepository accountTokenRepository;

    @Autowired
    private ApplicationService applicationService;


    public AccountToken getNonExpiredByPermanentToken(String permanentToken) {
        if (StringUtils.isEmpty(permanentToken)) {
            return null;
        }
        List<AccountTokenEntity> accountTokens = accountTokenRepository.findByPermanentToken(permanentToken);
        for (AccountTokenEntity at : accountTokens) {
            if (at.getPermanentTokenExpiresDateTime() == null || at.getPermanentTokenExpiresDateTime().isAfterNow()) {
                return toAccountToken(at);
            }
        }
        return null;
    }

    /**
     * @param accountId not null
     * @param appId     not null
     * @param deviceId  nullable
     * @return permanent token object
     */
    public Token createPermanentToken(String accountId, String appId, String deviceId) {
        DateTime now = DateTime.now();
        Integer expiresMinutes = null;

        Application application = applicationService.getById(appId);
        if (application != null) {
            expiresMinutes = application.getPermanentTokenExpiresMinutes();
        }
        if (expiresMinutes == null || expiresMinutes == 0) {
            expiresMinutes = PERMANENT_TOKEN_EXPIRES_MINUTES; //default expires minutes
        }

        DateTime expires = expiresMinutes > 0 ? now.plusMinutes(expiresMinutes) : null;
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

    public void expirePermanentTokenByDeviceId(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return;
        }
        List<AccountTokenEntity> entities = accountTokenRepository.findByDeviceId(deviceId)
                .stream()
                .filter(at -> at.getPermanentTokenExpiresDateTime() == null || at.getPermanentTokenExpiresDateTime().isAfterNow())
                .map(e -> {
                    e.setPermanentTokenExpiresDateTime(DateTime.now());
                    return e;
                })
                .collect(Collectors.toList());
        if (entities.size() > 0) {
            accountTokenRepository.save(entities);
        }
    }

    private AccountToken toAccountToken(AccountTokenEntity entity) {
        if (entity == null) {
            return null;
        }
        AccountToken accountToken = new AccountToken();
        accountToken.setId(entity.getId());
        accountToken.setAccountId(entity.getAccountId());
        accountToken.setAppId(entity.getAppId());
        accountToken.setDeviceId(entity.getDeviceId());
        accountToken.setPermanentToken(entity.getPermanentToken());
        accountToken.setPermanentTokenExpiresDateTime(entity.getPermanentTokenExpiresDateTime());
        accountToken.setCreatedDateTime(entity.getCreatedDateTime());
        return accountToken;
    }

}
