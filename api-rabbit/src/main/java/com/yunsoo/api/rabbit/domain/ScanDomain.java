package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.object.UserScanRecordObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/8/24
 * Descriptions:
 */
@Component
public class ScanDomain {

    @Autowired
    private RestClient dataAPIClient;

    public UserScanRecordObject createScanRecord(UserScanRecordObject userScanRecordObject) {
        userScanRecordObject.setId(null);
        userScanRecordObject.setCreatedDateTime(DateTime.now());
        return dataAPIClient.post("UserScanRecord", userScanRecordObject, UserScanRecordObject.class);
    }

    /**
     * @param productKey not null or empty
     * @param pageable   not null
     * @return page of UserScanRecordObject
     */
    public Page<UserScanRecordObject> getScanRecordsByProductKey(String productKey, Pageable pageable) {
        Assert.hasText(productKey, "productKey must not be null or empty");
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("product_key", productKey)
                .append(pageable)
                .build();
        return dataAPIClient.getPaged("UserScanRecord" + query, new ParameterizedTypeReference<List<UserScanRecordObject>>() {
        });
    }

    /**
     * @param userId   not null or empty
     * @param pageable not null
     * @return page of UserScanRecordObject
     */
    public Page<UserScanRecordObject> getScanRecordsByUserId(String userId, Pageable pageable) {
        Assert.hasText(userId, "userId must not be null or empty");
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("user_id", userId)
                .append(pageable)
                .build();
        return dataAPIClient.getPaged("UserScanRecord" + query, new ParameterizedTypeReference<List<UserScanRecordObject>>() {
        });
    }

}
