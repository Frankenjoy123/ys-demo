package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.AccountTokenObject;
import com.yunsoo.common.util.HashUtils;
import com.yunsoo.common.web.client.RestClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Created by  : Lijian
 * Created on  : 2015/4/21
 * Descriptions:
 */
@Component
public class AccountTokenDomain {

    @Value("${yunsoo.permanent_token.expires_minutes}")
    private int permanent_token_expires_minutes;

    @Autowired
    private RestClient dataAPIClient;


    public AccountTokenObject getNonExpiredByPermanentToken(String permanentToken) {
        if (!StringUtils.hasText(permanentToken)) {
            return null;
        }
        List<AccountTokenObject> accountTokenObjects = dataAPIClient.get("accounttoken?permanent_token={0}", new ParameterizedTypeReference<List<AccountTokenObject>>() {
        }, permanentToken);
        for (AccountTokenObject at : accountTokenObjects) {
            if (at.getPermanentTokenExpiresDateTime() == null || at.getPermanentTokenExpiresDateTime().isAfterNow()) {
                return at;
            }
        }
        return null;
    }

    public List<AccountTokenObject> getNonExpiredByDeviceId(String deviceId) {
        if (!StringUtils.hasText(deviceId)) {
            return new ArrayList<>();
        }
        List<AccountTokenObject> accountTokenObjects = dataAPIClient.get("accounttoken?device_id={0}", new ParameterizedTypeReference<List<AccountTokenObject>>() {
        }, deviceId);
        return accountTokenObjects.stream()
                .filter(at -> at.getPermanentTokenExpiresDateTime() == null || at.getPermanentTokenExpiresDateTime().isAfterNow())
                .collect(Collectors.toList());
    }

    public void expirePermanentTokenByDeviceId(String deviceId) {
        List<AccountTokenObject> accountTokenObjects = getNonExpiredByDeviceId(deviceId);
        for (AccountTokenObject at : accountTokenObjects) {
            expirePermanentTokenById(at.getId());
        }
    }

    public void expirePermanentTokenById(String accountTokenId) {
        AccountTokenObject accountTokenObject = new AccountTokenObject();
        accountTokenObject.setPermanentTokenExpiresDateTime(DateTime.now());
        dataAPIClient.patch("accounttoken/{id}", accountTokenObject, accountTokenId);
    }

    /**
     * @param accountId not null
     * @param appId     not null
     * @param deviceId  nullable
     * @return permanent token object
     */
    public AccountTokenObject create(String accountId, String appId, String deviceId) {
        return create(accountId, appId, deviceId, null);
    }

    /**
     * @param accountId      not null
     * @param appId          not null
     * @param deviceId       nullable
     * @param expiresMinutes nullable
     * @return permanent token object
     */
    public AccountTokenObject create(String accountId, String appId, String deviceId, Integer expiresMinutes) {
        DateTime now = DateTime.now();
        expiresMinutes = expiresMinutes == null ? permanent_token_expires_minutes : expiresMinutes;
        DateTime expires = expiresMinutes > 0 ? now.plusMinutes(expiresMinutes) : null;
        String permanentToken = HashUtils.sha1HexString(UUID.randomUUID().toString()); //random sha1
        AccountTokenObject accountTokenObject = new AccountTokenObject();
        accountTokenObject.setAccountId(accountId);
        accountTokenObject.setAppId(appId);
        accountTokenObject.setDeviceId(deviceId);
        accountTokenObject.setPermanentToken(permanentToken);
        accountTokenObject.setPermanentTokenExpiresDateTime(expires);
        accountTokenObject.setCreatedAccountId(accountId);
        accountTokenObject.setCreatedDateTime(now);
        return dataAPIClient.post("accounttoken", accountTokenObject, AccountTokenObject.class);
    }
}
