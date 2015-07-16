package com.yunsoo.api.domain;

import com.yunsoo.api.dto.PermissionInstance;
import com.yunsoo.common.data.object.GroupPermissionObject;
import com.yunsoo.common.data.object.GroupPermissionPolicyObject;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
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

    public GroupPermissionObject createGroupPermission(GroupPermissionObject groupPermissionObject) {
        return dataAPIClient.post("grouppermission", groupPermissionObject, GroupPermissionObject.class);
    }

    public void deleteGroupPermissionByGroupId(String groupId) {
        dataAPIClient.delete("grouppermission?group_id={groupId}", groupId);
    }

    public void deleteGroupPermissionById(String id) {
        dataAPIClient.delete("grouppermission/{id}", id);
    }

    public List<GroupPermissionPolicyObject> getGroupPermissionPolicies(String groupId) {
        return dataAPIClient.get("grouppermissionpolicy?group_id={groupId}", new ParameterizedTypeReference<List<GroupPermissionPolicyObject>>() {
        }, groupId);
    }

    public GroupPermissionPolicyObject createGroupPermissionPolicy(GroupPermissionPolicyObject groupPermissionPolicyObject) {
        return dataAPIClient.post("grouppermissionpolicy", groupPermissionPolicyObject, GroupPermissionPolicyObject.class);
    }

    public void deleteGroupPermissionPolicyByGroupId(String groupId) {
        deleteGroupPermissionPolicy(groupId, null, null);
    }

    public void deleteGroupPermissionPolicy(String groupId, String orgId, String policyCode) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("group_id", groupId)
                .append("org_id", orgId)
                .append("policy_code", policyCode)
                .build();
        dataAPIClient.delete("grouppermissionpolicy" + query);
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
