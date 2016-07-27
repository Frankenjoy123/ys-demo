package com.yunsoo.api.auth.domain;

import com.yunsoo.api.auth.dto.PermissionCheckRequest;
import com.yunsoo.api.client.AuthApiClient;
import com.yunsoo.api.security.permission.expression.PermissionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by:   Lijian
 * Created on:   2016-07-27
 * Descriptions:
 */
@Component
public class PermissionDomain {

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
