package com.yunsoo.api.domain;

import com.yunsoo.api.client.DataApiClient;
import com.yunsoo.common.data.object.AccountLoginLogObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

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
    private DataApiClient dataApiClient;

    private Log log = LogFactory.getLog(this.getClass());


    public Page<AccountLoginLogObject> getByAccountId(String accountId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("account_id", accountId)
                .append(pageable)
                .build();
        return dataApiClient.getPaged("accountLoginLog" + query, new ParameterizedTypeReference<List<AccountLoginLogObject>>() {
        });
    }

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
        accountLoginLogObject.setCreatedDateTime(DateTime.now());

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
        accountLoginLogObject.setCreatedDateTime(DateTime.now());

        saveLog(accountLoginLogObject);
    }

    private void saveLog(AccountLoginLogObject accountLoginLogObject) {
        try {
            accountLoginLogObject.setId(null);
            accountLoginLogObject.setCreatedDateTime(DateTime.now());
            dataApiClient.post("accountLoginLog", accountLoginLogObject, AccountLoginLogObject.class);
        } catch (Exception e) {
            log.error("accountLoginLog exception", e);
        }
    }
}
