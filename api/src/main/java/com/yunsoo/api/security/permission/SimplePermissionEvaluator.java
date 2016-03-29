package com.yunsoo.api.security.permission;

import com.yunsoo.api.security.AccountAuthentication;
import com.yunsoo.api.security.permission.expression.PermissionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression.OrgRestrictionExpression;
import com.yunsoo.common.util.StringFormatter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-03-28
 * Descriptions:
 */
public class SimplePermissionEvaluator implements PermissionEvaluator {

    private final Log log = LogFactory.getLog(getClass());
    public static final String TARGET_TYPE_ORG = "org";

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || targetDomainObject == null || permission == null || !(permission instanceof String)) {
            log.error("arguments issue: " + StringFormatter.formatMap("targetDomainObject", targetDomainObject, "permission", permission));
            return false;
        }
        AccountAuthentication accountAuthentication = (AccountAuthentication) authentication;
        RestrictionExpression restrictionExpression = null;
        PermissionExpression permissionExpression = PermissionExpression.parse((String) permission);

        if (targetDomainObject instanceof String) {
            restrictionExpression = RestrictionExpression.parse((String) targetDomainObject);
        }

        if (restrictionExpression == null) {
            log.error("restriction expression not detected");
            return false;
        }
        if (permissionExpression == null) {
            log.error("permission expression not well formatted");
            return false;
        }
        if (OrgRestrictionExpression.CURRENT.equals(restrictionExpression)) {
            restrictionExpression = new OrgRestrictionExpression(accountAuthentication.getPrincipal().getOrgId());
        }
        return accountAuthentication.checkPermission(restrictionExpression, permissionExpression);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || targetId == null || targetType == null || permission == null || !(permission instanceof String)) {
            log.error("argument issue: " + StringFormatter.formatMap("targetId", targetId, "targetType", targetType, "permission", permission));
            return false;
        }
        if (TARGET_TYPE_ORG.equals(targetType)) {
            return hasPermission(authentication, targetId, permission);
        }
        return false;
    }

}
