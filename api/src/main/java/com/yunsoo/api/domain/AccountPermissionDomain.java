package com.yunsoo.api.domain;

import com.yunsoo.api.dto.PermissionInstance;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.util.WildcardMatcher;
import com.yunsoo.common.data.object.AccountGroupObject;
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
import java.util.stream.Collectors;

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

    @Autowired
    private AccountGroupDomain accountGroupDomain;

    @Autowired
    private GroupPermissionDomain groupPermissionDomain;


    public List<AccountPermissionObject> getAccountPermissions(String accountId) {
        return dataAPIClient.get("accountpermission?account_id={accountId}", new ParameterizedTypeReference<List<AccountPermissionObject>>() {
        }, accountId);
    }

    public List<AccountPermissionPolicyObject> getAccountPermissionPolicies(String accountId) {
        return dataAPIClient.get("accountpermissionpolicy?account_id={accountId}", new ParameterizedTypeReference<List<AccountPermissionPolicyObject>>() {
        }, accountId);
    }

    public AccountPermissionObject createAccountPermission(AccountPermissionObject accountPermissionObject) {
        return dataAPIClient.post("accountpermission", accountPermissionObject, AccountPermissionObject.class);
    }

    public AccountPermissionPolicyObject createAccountPermissionPolicy(AccountPermissionPolicyObject accountPermissionPolicyObject) {
        return dataAPIClient.post("accountpermissionpolicy", accountPermissionPolicyObject, AccountPermissionPolicyObject.class);
    }

    public void deleteAccountPermissionByAccountId(String accountId) {
        dataAPIClient.delete("accountpermission?account_id={accountId}", accountId);
    }

    public void deleteAccountPermissionPolicyByAccountId(String accountId) {
        dataAPIClient.delete("accountpermissionpolicy?account_id={accountId}", accountId);
    }

    public void deleteAccountPermissionById(String id) {
        dataAPIClient.delete("accountpermission/{id}", id);
    }

    public void deleteAccountPermissionPolicy(String accountId, String orgId, String policyCode) {
        dataAPIClient.delete("accountpermissionpolicy?account_id={accountId}&org_id={orgId}&policy_code={pCode}",
                accountId, orgId, policyCode);
    }

    /**
     * get all the permissions related to the account, include group permissions and permission policies.
     *
     * @param accountId accountId
     * @return permissions
     */
    public List<PermissionInstance> getPermissionsByAccountId(String accountId) {
        List<PermissionInstance> permissions = new ArrayList<>();
        List<AccountPermissionObject> accountPermissions = getAccountPermissions(accountId);
        List<AccountPermissionPolicyObject> accountPermissionPolicies = getAccountPermissionPolicies(accountId);
        Map<String, PermissionPolicyObject> permissionPolicyMap = permissionDomain.getPermissionPolicyMap();
        accountPermissions.forEach(p -> {
            permissions.add(new PermissionInstance(p.getResourceCode(), p.getActionCode(), p.getOrgId()));
        });
        accountPermissionPolicies.stream().filter(pp -> permissionPolicyMap.containsKey(pp.getPolicyCode())).forEach(pp -> {
            permissionPolicyMap.get(pp.getPolicyCode()).getPermissions().forEach(po -> {
                permissions.add(new PermissionInstance(po.getResourceCode(), po.getActionCode(), pp.getOrgId()));
            });
        });

        //permissions from group
        List<AccountGroupObject> accountGroupObjects = accountGroupDomain.getAccountGroupByAccountId(accountId);
        accountGroupObjects.forEach(g -> {
            permissions.addAll(groupPermissionDomain.getAllGroupPermissions(g.getGroupId()));
        });
        return permissions;
    }

    public List<PermissionInstance> filterPermissionsByOrgId(List<PermissionInstance> permissions, String orgId) {
        return permissions.stream()
                .filter(p -> p.getOrgId() != null && (p.getOrgId().equals("*") || p.getOrgId().equals(orgId)))
                .collect(Collectors.toList());
    }

    public boolean hasPermission(String accountId, TPermission permission) {
        List<PermissionInstance> permissions = getPermissionsByAccountId(accountId);
        if (permissions != null && permissions.size() > 0) {
            String orgId = permission.getOrgId();
            String resourceCode = permission.getResourceCode();
            String actionCode = permission.getActionCode();
            if (resourceCode == null || actionCode == null) {
                return true; //anonymous
            }
            for (PermissionInstance pi : permissions) {
                boolean isOrgIdMatched = (orgId == null || "*".equals(pi.getOrgId()) || orgId.equals(pi.getOrgId()));
                if (!isOrgIdMatched) {
                    continue; //try next permission;
                }
                boolean isResourceMatched = WildcardMatcher.match(pi.getResourceCode(), resourceCode);
                boolean isActionMatched = WildcardMatcher.match(pi.getActionCode(), actionCode);
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


//    private List<String> filter(String expression, List<String> targets) {
//        return targets.stream().filter(i -> WildcardMatcher.match(expression, i)).collect(Collectors.toList());
//    }
//
//    private boolean anyMatch(String expression, List<String> targets) {
//        return targets.stream().anyMatch(i -> WildcardMatcher.match(expression, i));
//    }

}
