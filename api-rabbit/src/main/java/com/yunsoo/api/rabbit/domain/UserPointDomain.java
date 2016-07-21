package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.client.DataApiClient1;
import com.yunsoo.common.data.object.UserPointTransactionObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by:   Lijian
 * Created on:   2015/8/25
 * Descriptions:
 */
@Component
public class UserPointDomain {

    @Autowired
    private DataApiClient1 dataApiClient;

    public void addPointToUser(String userId, int point, String typeCode) {
        String createdAccountId = Constants.Ids.SYSTEM_ACCOUNT_ID;
        DateTime createdDateTime = DateTime.now();
        UserPointTransactionObject tranObject = new UserPointTransactionObject();
        tranObject.setUserId(userId);
        tranObject.setPoint(point);
        tranObject.setTypeCode(typeCode);
        tranObject.setCreatedAccountId(createdAccountId);
        tranObject.setCreatedDateTime(createdDateTime);
        UserPointTransactionObject newTranObject = dataApiClient.post("userpointtransaction", tranObject, UserPointTransactionObject.class);
        dataApiClient.post("userpointtransaction/{tranId}/commit", null, UserPointTransactionObject.class, newTranObject.getId());
    }


}
