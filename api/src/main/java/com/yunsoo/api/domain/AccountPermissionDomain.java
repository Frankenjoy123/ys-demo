package com.yunsoo.api.domain;

import com.yunsoo.api.object.TPermission;
import com.yunsoo.common.data.object.AccountPermissionObject;
import com.yunsoo.common.data.object.AccountPermissionPolicyObject;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/17
 * Descriptions:
 */
@Component
public class AccountPermissionDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private PermissionDomain permissionDomain;


    public List<AccountPermissionObject> getAccountPermissions(String accountId) {
        return dataAPIClient.get("accountpermission?account_id={accountId}", new ParameterizedTypeReference<List<AccountPermissionObject>>() {
        }, accountId);
    }

    public List<AccountPermissionPolicyObject> getAccountPermissionPolicies(String accountId) {
        return dataAPIClient.get("accountpermissionpolicy?account_id={accountId}", new ParameterizedTypeReference<List<AccountPermissionPolicyObject>>() {
        }, accountId);
    }

    /**
     * get all the permissions related to the account, include group permissions and permission policies.
     *
     * @param accountId accountId
     * @return permissions
     */
    public List<AccountPermissionObject> getAllAccountPermissions(String accountId) {
        List<AccountPermissionObject> permissions = new ArrayList<>();
        List<AccountPermissionObject> accountPermissions = getAccountPermissions(accountId);
        List<AccountPermissionPolicyObject> accountPermissionPolicies = getAccountPermissionPolicies(accountId);
        Map<String, PermissionPolicyObject> permissionPolicyMap = permissionDomain.getPermissionPolicyMap();
        permissions.addAll(accountPermissions);
        accountPermissionPolicies.stream().filter(pp -> permissionPolicyMap.containsKey(pp.getPolicyCode())).forEach(pp -> {
            permissionPolicyMap.get(pp.getPolicyCode()).getPermissions().forEach(po -> {
                AccountPermissionObject object = new AccountPermissionObject();
                object.setAccountId(pp.getAccountId());
                object.setOrgId(pp.getOrgId());
                object.setResourceCode(po.getResourceCode());
                object.setActionCode(po.getActionCode());
                object.setCreatedAccountId(pp.getCreatedAccountId());
                object.setCreatedDatetime(pp.getCreatedDatetime());
                permissions.add(object);
            });
        });
        return permissions;
    }

    public boolean hasPermission(String accountId, TPermission permission) {
        List<AccountPermissionObject> permissions = getAllAccountPermissions(accountId);
        if (permissions != null && permissions.size() > 0) {
            String orgId = permission.getOrgId();
            String resourceCode = permission.getResourceCode();
            String actionCode = permission.getActionCode();
            if (resourceCode == null || actionCode == null) {
                return true; //anonymous
            }
            for (AccountPermissionObject po : permissions) {
                boolean isOrgIdMatched = orgId == null || wildcardMatch(po.getOrgId(), orgId);
                if (!isOrgIdMatched) {
                    continue; //try next permission;
                }
                boolean isResourceMatched = wildcardMatch(po.getResourceCode(), resourceCode);
                boolean isActionMatched = wildcardMatch(po.getActionCode(), actionCode);
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


    /**
     * @param expression String with wildcard *, example: *, product*
     * @param target     String must not be null
     * @return if is match
     */
    private boolean wildcardMatch(String expression, String target) {
        return expression != null && Pattern.compile(expression.replace("*", "[\\w]*")).matcher(target).matches();
    }

}
