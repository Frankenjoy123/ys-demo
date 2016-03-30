package com.yunsoo.api.security.permission;

import com.yunsoo.api.dto.detectable.OrgIdDetectable;
import com.yunsoo.api.dto.Organization;
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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-03-28
 * Descriptions:
 */
public class SimplePermissionEvaluator implements PermissionEvaluator {

    private final Log log = LogFactory.getLog(getClass());

    public static final String TARGET_TYPE_ORG = "org";

    /**
     * @param authentication     AccountAuthentication
     * @param targetDomainObject [restrictionExpressionString|OrgIdDetectable], if it's null it'll be *
     * @param permission         [permissionExpressionString]
     * @return boolean
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || targetDomainObject == null || !(permission instanceof String)) {
            log.error("arguments issue: " + StringFormatter.formatMap("targetDomainObject", targetDomainObject, "permission", permission));
            return false;
        }
        RestrictionExpression restriction;
        if (targetDomainObject instanceof String) {
            String restrictionStr = (String) targetDomainObject;
            restriction = RestrictionExpression.parse(restrictionStr);
        } else {
            restriction = detectRestriction(targetDomainObject);
        }
        return hasPermission((AccountAuthentication) authentication, restriction, PermissionExpression.parse((String) permission));
    }

    /**
     * @param authentication AccountAuthentication
     * @param targetId       [orgRestrictionExpressionString|orgId|current|*], if it's null it'll be current as default.
     * @param targetType     [org]
     * @param permission     [permissionExpressionString]
     * @return boolean
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || targetType == null || !(permission instanceof String)) {
            log.error("arguments issue: " + StringFormatter.formatMap("targetId", targetId, "targetType", targetType, "permission", permission));
            return false;
        }
        if (TARGET_TYPE_ORG.equals(targetType)) {
            OrgRestrictionExpression restriction = targetId == null ? OrgRestrictionExpression.CURRENT : new OrgRestrictionExpression(targetId.toString());
            return hasPermission((AccountAuthentication) authentication, restriction, PermissionExpression.parse((String) permission));
        }
        return false;
    }

    private boolean hasPermission(AccountAuthentication accountAuthentication, RestrictionExpression restriction, PermissionExpression permission) {
        if (restriction == null) {
            log.error("restriction expression can not be detected");
            return false;
        }
        if (permission == null) {
            log.error("permission expression not well formatted");
            return false;
        }
        if (OrgRestrictionExpression.CURRENT.equals(restriction)) {
            restriction = new OrgRestrictionExpression(accountAuthentication.getPrincipal().getOrgId());
        }
        return accountAuthentication.checkPermission(restriction, permission);
    }

    private RestrictionExpression detectRestriction(Object targetDomainObject) {
        if (targetDomainObject instanceof Collection<?>) {
            List<RestrictionExpression> restrictions = ((Collection<?>) targetDomainObject)
                    .stream()
                    .map(this::detectRestriction)
                    .distinct()
                    .collect(Collectors.toList());
            return RestrictionExpression.collect(restrictions);
        }

        String orgId = null;
        if (targetDomainObject instanceof OrgIdDetectable) {
            orgId = ((OrgIdDetectable) targetDomainObject).getOrgId();
        } else if (targetDomainObject instanceof Organization) {
            orgId = ((Organization) targetDomainObject).getId();
        }
        return orgId == null ? null : new OrgRestrictionExpression(orgId);
    }
}
