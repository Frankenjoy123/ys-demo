package com.yunsoo.api.auth.service;

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
public class AuthApplicationService {

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private AuthApiClient authApiClient;

    public String getNameById(String appId) {
        if (StringUtils.isEmpty(appId)) {
            return null;
        }
        try {
            return authApiClient.get("application/{id}/name", String.class, appId);
        } catch (NotFoundException ignored) {
            log.warn("application name not found by id: " + appId);
            return null;
        }
    }
}
