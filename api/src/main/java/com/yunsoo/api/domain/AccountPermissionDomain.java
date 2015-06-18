package com.yunsoo.api.domain;

import com.yunsoo.api.dto.AccountPermission;
import com.yunsoo.api.dto.AccountPermissionPolicy;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.common.data.object.AccountPermissionObject;
import com.yunsoo.common.data.object.AccountPermissionPolicyObject;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/17
 * Descriptions:
 */
@Component
public class AccountPermissionDomain {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPermissionDomain.class);

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private PermissionDomain permissionDomain;

    public List<AccountPermission> getAccountPermissions(String accountId) {
        List<AccountPermissionObject> permissionObjects =
                dataAPIClient.get("accountpermission/{accountId}", new ParameterizedTypeReference<List<AccountPermissionObject>>() {
                }, accountId);
        return permissionObjects.stream().map(AccountPermission::new).collect(Collectors.toList());
    }

    public List<AccountPermissionPolicy> getAccountPermissionPolicies(String accountId) {
        List<AccountPermissionPolicyObject> permissionObjects =
                dataAPIClient.get("accountpermissionpolicy/{accountId}", new ParameterizedTypeReference<List<AccountPermissionPolicyObject>>() {
                }, accountId);
        return permissionObjects.stream().map(AccountPermissionPolicy::new).collect(Collectors.toList());
    }


    public boolean hasPermission(String accountId, TPermission permission) {
        List<TPermission> permissions = getAllAccountPermissions(accountId);
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

    public void checkPermission(String accountId, TPermission permission) {
        if (!hasPermission(accountId, permission)) {
            throw new ForbiddenException();
        }
    }


    private List<TPermission> getAllAccountPermissions(String accountId) {
        List<TPermission> permissions = new ArrayList<>();
        List<AccountPermission> accountPermissions = getAccountPermissions(accountId);
        List<AccountPermissionPolicy> accountPermissionPolicies = getAccountPermissionPolicies(accountId);
        Map<String, PermissionPolicyObject> permissionPolicyMap = permissionDomain.getPermissionPolicyMap();
        accountPermissions.stream().forEach(p -> {
            permissions.add(new TPermission(p.getOrgId(), p.getResourceCode(), p.getActionCode()));
        });
        accountPermissionPolicies.stream().filter(pp -> permissionPolicyMap.containsKey(pp.getPolicyCode())).forEach(pp -> {
            permissionPolicyMap.get(pp.getPolicyCode()).getPermissions().forEach(po -> {
                permissions.add(new TPermission(pp.getOrgId(), po.getResourceCode(), po.getActionCode()));
            });
        });
        return permissions;
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
