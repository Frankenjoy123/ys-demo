package com.yunsoo.api.auth.service;

import com.yunsoo.api.auth.dto.PermissionAllocation;
import com.yunsoo.api.auth.dto.PermissionCheckRequest;
import com.yunsoo.api.auth.dto.PermissionEntry;
import com.yunsoo.api.client.AuthApiClient;
import com.yunsoo.common.web.security.permission.PermissionEntry.Effect;
import com.yunsoo.common.web.security.permission.expression.PermissionExpression;
import com.yunsoo.common.web.security.permission.expression.PermissionExpression.SimplePermissionExpression;
import com.yunsoo.common.web.security.permission.expression.PrincipalExpression.AccountPrincipalExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression.OrgRestrictionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression.RegionRestrictionExpression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

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

    public List<PermissionEntry> getPermissionList() {
        return authApiClient.get("permission", new ParameterizedTypeReference<List<PermissionEntry>>() {
        });
    }

    public void allocateAdminPermissionOnCurrentOrgToAccount(String accountId) {
        if (StringUtils.isEmpty(accountId)) {
            return;
        }
        allocatePermissionByAccountId(
                accountId,
                OrgRestrictionExpression.CURRENT,
                SimplePermissionExpression.ADMIN,
                Effect.allow);
    }

    public void allocateAdminPermissionOnDefaultRegionToAccount(String accountId) {
        if (StringUtils.isEmpty(accountId)) {
            return;
        }
        allocatePermissionByAccountId(
                accountId,
                RegionRestrictionExpression.DEFAULT,
                SimplePermissionExpression.ADMIN,
                Effect.allow);
    }

    public void addOrgIdToDefaultRegion(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            return;
        }
        List<String> restrictions = Collections.singletonList(new OrgRestrictionExpression(orgId).toString());
        authApiClient.post("permission/region/default/restrictions", restrictions, String[].class);
    }

    private void allocatePermissionByAccountId(String accountId,
                                               RestrictionExpression restriction,
                                               PermissionExpression permission,
                                               Effect effect) {
        PermissionAllocation pa = new PermissionAllocation();
        pa.setPrincipal(new AccountPrincipalExpression(accountId).toString());
        pa.setRestriction(restriction.toString());
        pa.setPermission(permission.toString());
        pa.setEffect(effect.name());

        authApiClient.post("account/{accountId}/allocate", pa, PermissionAllocation.class, accountId);
    }

}
