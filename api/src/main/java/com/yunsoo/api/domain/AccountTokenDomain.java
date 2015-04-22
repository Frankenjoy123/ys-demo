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

    @Value("${yunsoo.api.permanent_token.expires_minutes}")
    private int permanent_token_expires_minutes;

    @Autowired
    private RestClient dataAPIClient;

    public AccountTokenObject getByPermanentToken(String permanentToken) {
        return dataAPIClient.get("accountToken?permanentToken={0}", AccountTokenObject.class, permanentToken);
    }

    /**
     * @param accountId not null
     * @param appId     not null
     * @param deviceId  nullable
     * @return permanent token object
     */
    public AccountTokenObject createPermanentToken(String accountId, String appId, String deviceId) {
        DateTime now = DateTime.now();
        DateTime expires = now.plusMinutes(permanent_token_expires_minutes);
        String permanentToken = HashUtils.sha1(UUID.randomUUID().toString()); //random sha1
        AccountTokenObject accountTokenObject = new AccountTokenObject();
        accountTokenObject.setAccountId(accountId);
        accountTokenObject.setAppId(appId);
        accountTokenObject.setDeviceId(deviceId);
        accountTokenObject.setPermanentToken(permanentToken);
        accountTokenObject.setPermanentTokenExpiresDateTime(expires);
        accountTokenObject.setCreatedAccountId(accountId);
        accountTokenObject.setCreatedDateTime(now);
        return dataAPIClient.post("accountToken", accountTokenObject, AccountTokenObject.class);
    }


}
