package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.security.permission.PermissionEntry;
import com.yunsoo.api.security.permission.PermissionService;
import com.yunsoo.api.security.permission.expression.PermissionExpression;
import com.yunsoo.api.security.permission.expression.PermissionExpression.SimplePermissionExpression;
import com.yunsoo.api.security.permission.expression.PrincipalExpression;
import com.yunsoo.api.security.permission.expression.PrincipalExpression.AccountPrincipalExpression;
import com.yunsoo.api.security.permission.expression.PrincipalExpression.GroupPrincipalExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression.OrgRestrictionExpression;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.PermissionAllocationObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

;
;

/**
 * Created by:   Lijian
 * Created on:   2016-03-21
 * Descriptions:
 */
@ObjectCacheConfig
@Component
public class PermissionAllocationDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AccountGroupDomain accountGroupDomain;


    //@Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'permissionEntries/'+#accountId)")
    public List<String> getPermissionEntriesByAccountId(String accountId) {
        return permissionService.getExpendedPermissionEntriesByAccountId(accountId)
                .stream()
                .map(PermissionEntry::toString)
                .collect(Collectors.toList());
    }

    /**
     * permission allocations of the account and it's groups
     *
     * @param accountId
     * @return
     */
    public List<PermissionAllocationObject> getAllPermissionAllocationsByAccountId(String accountId) {
        List<String> principals = new ArrayList<>();
        principals.add(new AccountPrincipalExpression(accountId).toString());
        principals.add(AccountPrincipalExpression.ANY.toString()); // account/*
        accountGroupDomain.getAccountGroupByAccountId(accountId).forEach(g -> {
            principals.add(new GroupPrincipalExpression(g.getGroupId()).toString());
        });

        return getPermissionAllocationsByPrincipal(principals);
    }

    public List<PermissionAllocationObject> getPermissionAllocationsByAccountId(String accountId) {
        return StringUtils.isEmpty(accountId)
                ? new ArrayList<>()
                : getPermissionAllocationsByPrincipal(new AccountPrincipalExpression(accountId).toString());
    }

    public List<PermissionAllocationObject> getPermissionAllocationsByGroupId(String groupId) {
        return StringUtils.isEmpty(groupId)
                ? new ArrayList<>()
                : getPermissionAllocationsByPrincipal(new GroupPrincipalExpression(groupId).toString());
    }

//
//    public void allocatePermissionToAccount(String accountId, String permission, String effect) {
//        Assert.hasText(accountId, "accountId not valid");
//        String principal = new AccountPrincipalExpression(accountId).toString();
//        String permissionExp = PermissionExpression.parse(permission).toString();
//
//    }

    public void allocateAdminPermissionToAccount(String accountId) {
        Assert.hasText(accountId, "accountId not valid");
        allocatePermission(
                new AccountPrincipalExpression(accountId),
                OrgRestrictionExpression.CURRENT,
                SimplePermissionExpression.ADMIN,
                PermissionEntry.Effect.allow);
    }

    private void allocatePermission(PrincipalExpression principal,
                                    RestrictionExpression restriction,
                                    PermissionExpression permission,
                                    PermissionEntry.Effect effect) {
        List<PermissionAllocationObject> pAOs = getPermissionAllocationsByPrincipal(principal.toString());
        for (PermissionAllocationObject pAO : pAOs) {
            if (restriction.toString().equals(pAO.getRestriction())
                    && permission.toString().equals(pAO.getPermission())
                    && effect.name().equals(pAO.getEffect().name())) {
                return; //already has the same allocation item
            }
        }
        PermissionAllocationObject permissionAllocationObject = new PermissionAllocationObject();
        permissionAllocationObject.setPrincipal(principal.toString());
        permissionAllocationObject.setRestriction(restriction.toString());
        permissionAllocationObject.setPermission(permission.toString());
        permissionAllocationObject.setEffect(PermissionAllocationObject.Effect.valueOf(effect.name()));
        permissionAllocationObject.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        createPermissionAllocation(permissionAllocationObject);
    }


    //region private data api methods

    private PermissionAllocationObject getPermissionAllocationById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        try {
            return dataAPIClient.get("permissionAllocation/{id}", PermissionAllocationObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    private List<PermissionAllocationObject> getPermissionAllocationsByPrincipal(String principal) {
        if (StringUtils.isEmpty(principal)) {
            return new ArrayList<>();
        }
        return dataAPIClient.get("permissionAllocation?principal={p}", new ParameterizedTypeReference<List<PermissionAllocationObject>>() {
        }, principal);
    }

    private List<PermissionAllocationObject> getPermissionAllocationsByPrincipal(List<String> principals) {
        if (principals == null || principals.size() == 0) {
            return new ArrayList<>();
        }
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("principal_in", principals)
                .build();
        return dataAPIClient.get("permissionAllocation" + query, new ParameterizedTypeReference<List<PermissionAllocationObject>>() {
        }, principals);
    }

    private PermissionAllocationObject createPermissionAllocation(PermissionAllocationObject permissionAllocationObject) {
        permissionAllocationObject.setId(null);
        permissionAllocationObject.setCreatedDateTime(DateTime.now());
        return dataAPIClient.post("permissionAllocation", permissionAllocationObject, PermissionAllocationObject.class);
    }

    private void deletePermissionAllocationById(String id) {
        if (!StringUtils.isEmpty(id)) {
            dataAPIClient.delete("permissionAllocation/{id}", id);
        }
    }

    //endregion
}
