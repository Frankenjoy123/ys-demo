package com.yunsoo.api.auth.domain;

import com.yunsoo.api.auth.dto.Device;
import com.yunsoo.api.client.AuthApiClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

}
