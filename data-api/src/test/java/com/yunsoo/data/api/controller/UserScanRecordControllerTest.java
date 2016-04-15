package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserScanRecordObject;
import com.yunsoo.common.util.KeyGenerator;
import com.yunsoo.common.util.ObjectIdGenerator;
import com.yunsoo.data.api.Constants;
import com.yunsoo.data.api.ControllerTestBase;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.UUID;

/**
 * Created by:   Lijian
 * Created on:   2016-03-10
 * Descriptions:
 */
public class UserScanRecordControllerTest extends ControllerTestBase {

    @Test
    public void test_All() {
        UserScanRecordObject newRecord = new UserScanRecordObject();
        newRecord.setUserId(Constants.Ids.ANONYMOUS_USER_ID);
        newRecord.setProductKey(KeyGenerator.getNew());
        newRecord.setProductKeyBatchId(ObjectIdGenerator.getNew());
        newRecord.setProductBaseId(ObjectIdGenerator.getNew());
        newRecord.setAppId(ObjectIdGenerator.getNew());
        newRecord.setYsid(UUID.randomUUID().toString().replace("-", ""));

        UserScanRecordObject savedRecord = dataAPIClient.post("userScanRecord", newRecord, UserScanRecordObject.class);
        System.out.println("userScanRecord saved with id: " + savedRecord.getId());

        UserScanRecordObject getRecord = dataAPIClient.get("userScanRecord/{id}", UserScanRecordObject.class, savedRecord.getId());

        List<UserScanRecordObject> records = dataAPIClient.get("userScanRecord?product_key={key}", new ParameterizedTypeReference<List<UserScanRecordObject>>() {
        }, newRecord.getProductKey());

        assert records.size() > 0;
    }

}
