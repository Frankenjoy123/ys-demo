package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.UserScanRecordObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by:   Haitao
 * Created on:   2/26/2016
 * Descriptions:
 */
@Component
public class UserScanDomain {
    @Autowired
    private RestClient dataApiClient;

    public UserScanRecordObject getScanRecordById(String id) {
        if (id == null) {
            throw new BadRequestException("scan record id is not valid");
        }
        return dataApiClient.get("userScanRecord/{id}", UserScanRecordObject.class, id);
    }
}
