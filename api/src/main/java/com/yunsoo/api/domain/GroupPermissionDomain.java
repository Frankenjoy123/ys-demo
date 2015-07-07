package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.GroupPermissionObject;
import com.yunsoo.common.data.object.GroupPermissionPolicyObject;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/25
 * Descriptions:
 */
@Component
public class GroupPermissionDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private PermissionDomain permissionDomain;

    public List<GroupPermissionObject> getGroupPermissions(String groupId) {
        return dataAPIClient.get("grouppermission?group_id={groupId}", new ParameterizedTypeReference<List<GroupPermissionObject>>() {
        }, groupId);
    }

    public List<GroupPermissionPolicyObject> getGroupPermissionPolicies(String groupId) {
        return dataAPIClient.get("grouppermissionpolicy?group_id={groupId}", new ParameterizedTypeReference<List<GroupPermissionPolicyObject>>() {
        }, groupId);
    }

    public GroupPermissionObject createGroupPermission(GroupPermissionObject groupPermissionObject) {
        return dataAPIClient.post("grouppermission", groupPermissionObject, GroupPermissionObject.class);
    }

    public GroupPermissionPolicyObject createGroupPermissionPolicy(GroupPermissionPolicyObject groupPermissionPolicyObject) {
        return dataAPIClient.post("grouppermissionpolicy", groupPermissionPolicyObject, GroupPermissionPolicyObject.class);
    }

    public void deleteGroupPermission(String id) {
        dataAPIClient.delete("grouppermission?id={id}",id);
    }

    public void deleteGroupPermissionPolicy(String id) {
        dataAPIClient.delete("grouppermissionpolicy?id={id}",id);
    }

    public List<GroupPermissionObject> getAllGroupPermissions(String groupId) {
        List<GroupPermissionObject> permissions = new ArrayList<>();
        List<GroupPermissionObject> accountPermissions = getGroupPermissions(groupId);
        List<GroupPermissionPolicyObject> accountPermissionPolicies = getGroupPermissionPolicies(groupId);
        Map<String, PermissionPolicyObject> permissionPolicyMap = permissionDomain.getPermissionPolicyMap();
        permissions.addAll(accountPermissions);
        accountPermissionPolicies.stream().filter(pp -> permissionPolicyMap.containsKey(pp.getPolicyCode())).forEach(pp -> {
            permissionPolicyMap.get(pp.getPolicyCode()).getPermissions().forEach(po -> {
                GroupPermissionObject object = new GroupPermissionObject();
                object.setGroupId(pp.getGroupId());
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
}
