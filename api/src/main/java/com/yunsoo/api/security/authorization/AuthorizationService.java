package com.yunsoo.api.security.authorization;

import com.yunsoo.api.security.permission.PermissionEntry;
import com.yunsoo.api.security.permission.PermissionEntry.Effect;
import com.yunsoo.api.security.permission.expression.PermissionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;
import org.springframework.stereotype.Service;

/**
 * Created by:   Lijian
 * Created on:   2016-03-23
 * Descriptions:
 */
@Service
public class AuthorizationService {


    public Effect check(PermissionEntry permissionEntry, RestrictionExpression restriction, PermissionExpression permission) {


        return null;
    }
}
