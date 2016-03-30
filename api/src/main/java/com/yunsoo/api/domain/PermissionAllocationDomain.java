package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.security.permission.PermissionEntry;
import com.yunsoo.api.security.permission.PermissionService;
import com.yunsoo.api.security.permission.expression.PrincipalExpression.AccountPrincipalExpression;
import com.yunsoo.api.security.permission.expression.PrincipalExpression.GroupPrincipalExpression;
import com.yunsoo.common.data.object.PermissionAllocationObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    //@Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'permissionEntries/'+#accountId)")
    public List<String> getPermissionEntriesByAccountId(String accountId) {
        List<PermissionEntry> permissionEntries = permissionService.getExpendedPermissionEntriesByAccountId(accountId);
        return permissionEntries.stream().map(PermissionEntry::toString).collect(Collectors.toList());
    }

    public List<PermissionAllocationObject> getPermissionAllocations(String accountId, List<String> groupIds) {
        List<String> principals = new ArrayList<>();
        if (!StringUtils.isEmpty(accountId)) {
            principals.add(new AccountPrincipalExpression(accountId).toString());
        }
        if (groupIds != null && groupIds.size() > 0) {
            groupIds.forEach(g -> {
                principals.add(new GroupPrincipalExpression(g).toString());
            });
        }
        return principals.size() == 0 ? new ArrayList<>() : getPermissionAllocationsByPrincipal(principals);
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

//    public void allocatePermissionByAccount(String accountId, String restriction, String permission, String effect) {
//        String principal = "account/" + accountId;
//
//
//    }


    private PermissionAllocationObject getPermissionAllocationById(String id) {
        return dataAPIClient.get("permissionAllocation/{id}", PermissionAllocationObject.class, id);
    }

    private List<PermissionAllocationObject> getPermissionAllocationsByPrincipal(String principal) {
        return dataAPIClient.get("permissionAllocation?principal={p}", new ParameterizedTypeReference<List<PermissionAllocationObject>>() {
        }, principal);
    }

    private List<PermissionAllocationObject> getPermissionAllocationsByPrincipal(List<String> principals) {
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
}
