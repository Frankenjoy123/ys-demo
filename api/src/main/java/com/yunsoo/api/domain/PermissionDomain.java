package com.yunsoo.api.domain;

import com.yunsoo.api.object.TPermission;
import com.yunsoo.common.data.object.AccountPermissionObject;
import com.yunsoo.common.data.object.AccountPermissionPolicyObject;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.RestErrorResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
@Component
public class PermissionDomain {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionDomain.class);

    @Autowired
    private RestClient dataAPIClient;

    public List<TPermission> getAccountPermissionsByAccountId(String accountId) {
        List<TPermission> permissions = new ArrayList<>();
        try {
            AccountPermissionObject[] permissionObjects =
                    dataAPIClient.get("accountpermission/permission/{accountId}", AccountPermissionObject[].class, accountId);
            AccountPermissionPolicyObject[] permissionPolicyObjects =
                    dataAPIClient.get("accountpermission/permissionpolicy/{accountId}", AccountPermissionPolicyObject[].class, accountId);
            Map<String, PermissionPolicyObject> permissionPolicies = getPermissionPolicies();
            for (AccountPermissionObject p : permissionObjects) {
                TPermission permission = new TPermission();
                permission.setOrgId(p.getOrgId());
                permission.setResourceCode(p.getResourceCode());
                permission.setActionCode(p.getActionCode());
                permissions.add(permission);
            }
            for (AccountPermissionPolicyObject pp : permissionPolicyObjects) {
                if (permissionPolicies.containsKey(pp.getPolicyCode())) {
                    permissionPolicies.get(pp.getPolicyCode()).getPermissions().forEach(po -> {
                        TPermission permission = new TPermission();
                        permission.setOrgId(pp.getOrgId());
                        permission.setResourceCode(po.getResourceCode());
                        permission.setActionCode(po.getActionCode());
                        permissions.add(permission);
                    });
                }
            }

        } catch (RestErrorResultException ex) {
            LOGGER.error("getAccountPermissionsByAccountId error", ex);
        }
        return permissions;
    }

    public Map<String, PermissionPolicyObject> getPermissionPolicies() {
        Map<String, PermissionPolicyObject> permissionPolicies = new HashMap<>();
        List<PermissionPolicyObject> permissionPolicyObjects = dataAPIClient.get("permission/policy",
                new ParameterizedTypeReference<List<PermissionPolicyObject>>() {
                });
        permissionPolicyObjects.forEach(o -> {
            if (o == null) {
                return;
            }
            if (o.getPermissions() == null) {
                o.setPermissions(new ArrayList<>());
            }
            String pCode = o.getCode();
            if (permissionPolicies.containsKey(pCode)) {
                permissionPolicies.get(pCode).getPermissions().addAll(o.getPermissions());
            } else {
                permissionPolicies.put(pCode, o);
            }
        });

        return permissionPolicies;
    }

    public boolean hasPermission(String accountId, TPermission permission) {
        List<TPermission> permissions = getAccountPermissionsByAccountId(accountId);
        if (permissions != null && permissions.size() > 0) {
            String orgId = permission.getOrgId();
            String resourceCode = permission.getResourceCode();
            String actionCode = permission.getActionCode();
            if (resourceCode == null || actionCode == null) {
                return true; //anonymous
            }
            for (TPermission p : permissions) {
                boolean isOrgIdMatched = orgId == null || wildcardMatch(p.getOrgId(), orgId);
                if (!isOrgIdMatched) {
                    continue; //try next permission;
                }
                boolean isResourceMatched = wildcardMatch(p.getResourceCode(), resourceCode);
                boolean isActionMatched = wildcardMatch(p.getActionCode(), actionCode);
                if (!isResourceMatched || !isActionMatched) {
                    continue; //try next permission;
                }

                return true; //matched
            }

        }
        return false;
    }

    /**
     * @param expression String with wildcard *, example: *, product*
     * @param target     String must not be null
     * @return if is match
     */
    private boolean wildcardMatch(String expression, String target) {
        return expression != null && Pattern.compile(expression.replace("*", "[\\w]*")).matcher(target).matches();
    }

}
