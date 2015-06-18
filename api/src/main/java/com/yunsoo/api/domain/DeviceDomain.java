package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.DeviceObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/11
 * Descriptions:
 */
@Component
public class DeviceDomain {

    @Autowired
    private RestClient dataAPIClient;

    public DeviceObject getById(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return null;
        }
        try {
            return dataAPIClient.get("device/{id}", DeviceObject.class, deviceId);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public Page<List<DeviceObject>> getByFilterPaged(String orgId, String loginAccountId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("login_account_id", loginAccountId)
                .append(pageable)
                .build();
        return dataAPIClient.getPaged("device" + query, new ParameterizedTypeReference<List<DeviceObject>>() {
        });
    }

    public DeviceObject create(DeviceObject deviceObject) {
        return dataAPIClient.post("device", deviceObject, DeviceObject.class);
    }

    public void update(DeviceObject deviceObject) {
        dataAPIClient.put("device", deviceObject, DeviceObject.class);
    }
}