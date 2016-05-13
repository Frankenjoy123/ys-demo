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

    public Page<DeviceObject> getByFilterPaged(String orgId, String loginAccountId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("login_account_id", loginAccountId)
                .append(pageable)
                .build();
        return dataAPIClient.getPaged("device" + query, new ParameterizedTypeReference<List<DeviceObject>>() {
        });
    }

    public void patchUpdate(DeviceObject deviceObject) {
        try {
            dataAPIClient.patch("device/{id}", deviceObject, deviceObject.getId());
        } catch (NotFoundException ex) {
            throw new NotFoundException("device not found by id: " + deviceObject.getId());
        }
    }

    public void put(DeviceObject deviceObject) {
        dataAPIClient.put("device/{id}", deviceObject, deviceObject.getId());
    }

    public void delete(String id) {
        if (StringUtils.hasText(id)) {
            dataAPIClient.delete("device/{id}", id);
        }
    }
}
