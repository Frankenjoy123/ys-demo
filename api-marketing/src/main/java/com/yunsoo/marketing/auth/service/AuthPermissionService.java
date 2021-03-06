package com.yunsoo.marketing.auth.service;

import com.yunsoo.common.web.security.permission.expression.PermissionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression;
import com.yunsoo.marketing.auth.dto.PermissionCheckRequest;
import com.yunsoo.marketing.client.AuthApiClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by:   Lijian
 * Created on:   2016-07-27
 * Descriptions:
 */
@Service
public class AuthPermissionService {

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private AuthApiClient authApiClient;

    public boolean checkPermission(RestrictionExpression restriction, PermissionExpression permission) {
        if (restriction == null || permission == null) {
            return false;
        }
        try {
            return authApiClient.post("permission/check", new PermissionCheckRequest(restriction.toString(), permission.toString()), boolean.class);
        } catch (Exception e) {
            log.error("permission check failed with exception", e);
            return false;
        }
    }

}
