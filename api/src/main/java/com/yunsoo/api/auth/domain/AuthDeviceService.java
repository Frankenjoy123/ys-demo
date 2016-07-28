package com.yunsoo.api.auth.domain;

import com.yunsoo.api.auth.dto.Device;
import com.yunsoo.api.auth.dto.DeviceRegisterRequest;
import com.yunsoo.api.client.AuthApiClient;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-07-28
 * Descriptions:
 */
@Service
public class AuthDeviceService {

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private AuthApiClient authApiClient;


    public Device getById(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return null;
        }
        try {
            return authApiClient.get("device/{id}", Device.class, deviceId);
        } catch (NotFoundException ex) {
            log.warn("device not found by id: " + deviceId);
            return null;
        }
    }

    public Page<Device> getByFilter(String orgId, String authAccountId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("auth_account_id", authAccountId)
                .append(pageable)
                .build();
        return authApiClient.getPaged("device" + query, new ParameterizedTypeReference<List<Device>>() {
        });
    }

    public Device register(DeviceRegisterRequest request) {
        return authApiClient.post("device/register", request, Device.class);
    }

    public void unregister(String deviceId) {
        if (!StringUtils.isEmpty(deviceId)) {
            authApiClient.delete("device/{id}", deviceId);
        }
    }
}
