package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.AccountTokenObject;
import com.yunsoo.common.util.HashUtils;
import com.yunsoo.common.web.client.RestClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;


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

    public AccountTokenObject getByPermanentToken(String permanentToken) {
        return dataAPIClient.get("accounttoken?permanent_token={0}", AccountTokenObject.class, permanentToken);
    }

    /**
     * @param accountId not null
     * @param appId     not null
     * @param deviceId  nullable
     * @return permanent token object
     */
    public AccountTokenObject create(String accountId, String appId, String deviceId) {
        return create(accountId, appId, deviceId, permanent_token_expires_minutes);
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
        DateTime expires = expiresMinutes != null && expiresMinutes > 0 ? now.plusMinutes(expiresMinutes) : null;
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
