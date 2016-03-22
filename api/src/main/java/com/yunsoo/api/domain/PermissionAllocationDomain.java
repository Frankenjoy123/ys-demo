package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.common.data.object.PermissionAllocationObject;
import com.yunsoo.common.web.client.RestClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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


    public List<PermissionAllocationObject> getPermissionAllocationsByAccountId(String accountId) {
        return StringUtils.isEmpty(accountId) ? new ArrayList<>() : getPermissionAllocationsByPrincipal("account/" + accountId);
    }

    public List<PermissionAllocationObject> getPermissionAllocationsByGroupId(String groupId) {
        return StringUtils.isEmpty(groupId) ? new ArrayList<>() : getPermissionAllocationsByPrincipal("group/" + groupId);
    }

    public void allocatePermissionByAccount(String accountId, String restriction, String permission, String effect) {
        String principal = "account/" + accountId;


    }


    private PermissionAllocationObject getPermissionAllocationById(String id) {
        return dataAPIClient.get("permissionAllocation/{id}", PermissionAllocationObject.class, id);
    }

    private List<PermissionAllocationObject> getPermissionAllocationsByPrincipal(String principal) {
        return dataAPIClient.get("permissionAllocation?principal={p}", new ParameterizedTypeReference<List<PermissionAllocationObject>>() {
        }, principal);
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
