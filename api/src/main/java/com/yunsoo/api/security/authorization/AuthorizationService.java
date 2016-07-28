package com.yunsoo.api.security.authorization;

import com.yunsoo.api.auth.service.AuthPermissionService;
import com.yunsoo.api.security.authentication.AccountAuthentication;
import com.yunsoo.common.web.security.permission.PermissionEntry;
import com.yunsoo.common.web.security.permission.expression.PermissionExpression;
import com.yunsoo.common.web.security.permission.expression.PrincipalExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression.CollectionRestrictionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression.OrgRestrictionExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-03-23
 * Descriptions: referred by AccountAuthentication
 */
@Service
public class AuthorizationService {

    @Autowired
    private AuthPermissionService authPermissionService;

    /**
     * @param accountAuthentication AccountAuthentication
     * @return PermissionEntry list of the current account
     */
    public List<PermissionEntry> getPermissionEntries(AccountAuthentication accountAuthentication) {
        return authPermissionService.getPermissionList()
                .stream()
                .map(e -> {
                    PermissionEntry pe = new PermissionEntry();
                    if (e != null) {
                        pe.setId(e.getId());
                        pe.setPrincipal(PrincipalExpression.parse(e.getPrincipal()));
                        pe.setRestriction(RestrictionExpression.parse(e.getRestriction()));
                        pe.setPermission(PermissionExpression.parse(e.getPermission()));
                        pe.setEffect(PermissionEntry.Effect.valueOf(e.getEffect()));
                    }
                    return pe;
                })
                .filter(PermissionEntry::isValid)
                .collect(Collectors.toList());
    }

    /**
     * main entrance to the checkPermission logic
     *
     * @param accountAuthentication AccountAuthentication
     * @param restriction           [OrgRestrictionExpression|CollectionRestrictionExpression]
     * @param permission            [SimplePermissionExpression|CollectionPermissionExpression]
     * @return boolean    [true: has permission, false: has no permission]
     */
    public boolean checkPermission(AccountAuthentication accountAuthentication, RestrictionExpression restriction, PermissionExpression permission) {
        if (restriction == null || permission == null) {
            return false;
        }
        if (restriction instanceof RestrictionExpression.CollectionRestrictionExpression) {
            for (RestrictionExpression r : ((RestrictionExpression.CollectionRestrictionExpression) restriction).distinct().getExpressions()) {
                if (!checkPermission(accountAuthentication, r, permission)) {
                    return false;
                }
            }
            return true;
        } else if (permission instanceof PermissionExpression.CollectionPermissionExpression) {
            for (PermissionExpression p : ((PermissionExpression.CollectionPermissionExpression) permission).distinct().getExpressions()) {
                if (!checkPermission(accountAuthentication, restriction, p)) {
                    return false;
                }
            }
            return true;
        } else {
            restriction = fixOrgRestriction(restriction, accountAuthentication.getPrincipal().getOrgId());
            for (PermissionEntry entry : accountAuthentication.getPermissionEntries()) {
                PermissionEntry.Effect effect = entry.check(restriction, permission);
                if (effect != null) {
                    return effect.equals(PermissionEntry.Effect.allow);
                }
            }
        }
        return false;
    }

    public boolean checkPermission(RestrictionExpression restriction, PermissionExpression permission) {
        return restriction != null && permission != null && authPermissionService.checkPermission(restriction, permission);
    }

    public RestrictionExpression fixOrgRestriction(RestrictionExpression restriction, String orgId) {
        if (restriction instanceof CollectionRestrictionExpression) {
            List<RestrictionExpression> subRestrictions = new ArrayList<>();
            boolean hasCurrent = false;
            for (RestrictionExpression r : ((CollectionRestrictionExpression) restriction).getExpressions()) {
                if (OrgRestrictionExpression.CURRENT.equals(r)) {
                    r = new OrgRestrictionExpression(orgId);
                    hasCurrent = true;
                }
                subRestrictions.add(r);
            }
            if (hasCurrent) {
                restriction = new CollectionRestrictionExpression(subRestrictions);
            }
        } else if (OrgRestrictionExpression.CURRENT.equals(restriction)) {
            restriction = new OrgRestrictionExpression(orgId);
        }
        return restriction;
    }

}
