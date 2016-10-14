package com.yunsoo.marketing.security.authorization;

import com.yunsoo.common.web.security.permission.expression.PermissionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression;
import com.yunsoo.marketing.auth.service.AuthPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by:   Lijian
 * Created on:   2016-03-23
 * Descriptions: referred by AccountAuthentication
 */
@Service
public class AuthorizationService {

    @Autowired
    private AuthPermissionService authPermissionService;

    public boolean checkPermission(RestrictionExpression restriction, PermissionExpression permission) {
        return restriction != null && permission != null && authPermissionService.checkPermission(restriction, permission);
    }


}
