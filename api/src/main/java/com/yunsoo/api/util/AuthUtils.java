package com.yunsoo.api.util;

import com.yunsoo.api.security.AccountAuthentication;
import com.yunsoo.api.security.AuthAccount;
import com.yunsoo.api.security.permission.expression.PermissionExpression;
import com.yunsoo.api.security.permission.expression.PermissionExpression.SimplePermissionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression.OrgRestrictionExpression;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by:   Lijian
 * Created on:   2016-03-29
 * Descriptions: shortcuts of some method
 */
public final class AuthUtils {

    public static AccountAuthentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AccountAuthentication)) {
            throw new UnauthorizedException();
        }
        return (AccountAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public static AuthAccount getCurrentAccount() {
        return getAuthentication().getPrincipal();
    }

    public static void checkPermission(RestrictionExpression restriction, PermissionExpression permission) {
        AccountAuthentication accountAuthentication = getAuthentication();
        if (!accountAuthentication.checkPermission(restriction, permission)) {
            throw new ForbiddenException("access denied");
        }
    }

    public static void checkPermission(String orgId, String permission) {
        RestrictionExpression restrictionExp = new OrgRestrictionExpression(orgId);
        PermissionExpression permissionExp = new SimplePermissionExpression(permission);
        checkPermission(restrictionExp, permissionExp);
    }

    public static void checkPermission(String orgId, String resource, String action) {
        RestrictionExpression restrictionExp = new OrgRestrictionExpression(orgId);
        PermissionExpression permissionExp = new SimplePermissionExpression(resource, action);
        checkPermission(restrictionExp, permissionExp);
    }
}
