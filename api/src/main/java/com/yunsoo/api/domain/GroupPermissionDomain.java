package com.yunsoo.api.domain;

import com.yunsoo.api.dto.PermissionInstance;
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

    public void deleteGroupPermissionByGroupId(String groupId) {
        dataAPIClient.delete("grouppermission?group_id={groupid}",groupId);
    }

    public void deleteGroupPermissionPolicyByGroupId(String groupId) {
        dataAPIClient.delete("grouppermissionpolicy?group_id={groupid}",groupId);
    }

    public void deleteGroupPermissionById(String id) {
        dataAPIClient.delete("grouppermission/{id}", id);
    }

    public void deleteGroupPermissionPolicyById(String id) {
        dataAPIClient.delete("grouppermissionpolicy/{id}", id);
    }

    public List<PermissionInstance> getAllGroupPermissions(String groupId) {
        List<PermissionInstance> permissions = new ArrayList<>();
        List<GroupPermissionObject> groupPermissions = getGroupPermissions(groupId);
        List<GroupPermissionPolicyObject> groupPermissionPolicies = getGroupPermissionPolicies(groupId);
        Map<String, PermissionPolicyObject> permissionPolicyMap = permissionDomain.getPermissionPolicyMap();
        groupPermissions.forEach(p -> {
            permissions.add(new PermissionInstance(p.getResourceCode(), p.getActionCode(), p.getOrgId()));
        });
        groupPermissionPolicies.stream().filter(pp -> permissionPolicyMap.containsKey(pp.getPolicyCode())).forEach(pp -> {
            permissionPolicyMap.get(pp.getPolicyCode()).getPermissions().forEach(po -> {
                permissions.add(new PermissionInstance(po.getResourceCode(), po.getActionCode(), pp.getOrgId()));
            });
        });
        return permissions;
    }
}
