package com.yunsoo.api.domain;

import com.yunsoo.api.client.DataAPIClient;
import com.yunsoo.common.data.object.AccountLoginLogObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by:   Lijian
 * Created on:   2016-05-03
 * Descriptions:
 */
@Component
public class AccountLoginLogDomain {

    private static final String CHANNEL_PASSWORD = "password";
    private static final String CHANNEL_TOKEN = "token";

    @Autowired
    private DataAPIClient dataAPIClient;

    private Log log = LogFactory.getLog(this.getClass());

    public void savePasswordLogin(String accountId,
                                  String appId,
                                  String deviceId,
                                  String ip,
                                  String userAgent) {
        AccountLoginLogObject accountLoginLogObject = new AccountLoginLogObject();
        accountLoginLogObject.setAccountId(accountId);
        accountLoginLogObject.setAppId(appId);
        accountLoginLogObject.setChannel(CHANNEL_PASSWORD);
        accountLoginLogObject.setDeviceId(deviceId);
        accountLoginLogObject.setIp(ip);
        accountLoginLogObject.setUserAgent(userAgent);
        accountLoginLogObject.setCreatedDatetime(DateTime.now());

        saveLog(accountLoginLogObject);
    }

    public void saveTokenLogin(String accountId,
                               String appId,
                               String deviceId,
                               String ip,
                               String userAgent) {
        AccountLoginLogObject accountLoginLogObject = new AccountLoginLogObject();
        accountLoginLogObject.setAccountId(accountId);
        accountLoginLogObject.setAppId(appId);
        accountLoginLogObject.setChannel(CHANNEL_TOKEN);
        accountLoginLogObject.setDeviceId(deviceId);
        accountLoginLogObject.setIp(ip);
        accountLoginLogObject.setUserAgent(userAgent);
        accountLoginLogObject.setCreatedDatetime(DateTime.now());

        saveLog(accountLoginLogObject);
    }

    private void saveLog(AccountLoginLogObject accountLoginLogObject) {
        try {
            accountLoginLogObject.setId(null);
            accountLoginLogObject.setCreatedDatetime(DateTime.now());
            dataAPIClient.post("accountLoginLog", accountLoginLogObject, AccountLoginLogObject.class);
        } catch (Exception e) {
            log.error("accountLoginLog exception", e);
        }
    }
}
