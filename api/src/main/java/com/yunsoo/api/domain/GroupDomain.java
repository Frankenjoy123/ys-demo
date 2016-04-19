package com.yunsoo.api.domain;

import com.yunsoo.api.client.DataAPIClient;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.GroupObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/24
 * Descriptions:
 */
@Component
public class GroupDomain {

    @Autowired
    private DataAPIClient dataAPIClient;

    @Autowired
    private PermissionAllocationDomain permissionAllocationDomain;


    public GroupObject getById(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return null;
        }
        try {
            return dataAPIClient.get("group/{id}", GroupObject.class, groupId);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public List<GroupObject> getByOrgId(String orgId) {
        return dataAPIClient.get("group?org_id={orgId}", new ParameterizedTypeReference<List<GroupObject>>() {
        }, orgId);
    }

    public GroupObject create(GroupObject groupObject) {
        groupObject.setId(null);
        groupObject.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        groupObject.setCreatedDateTime(DateTime.now());
        groupObject.setModifiedAccountId(null);
        groupObject.setModifiedDatetime(null);
        return dataAPIClient.post("group", groupObject, GroupObject.class);
    }

    public void patchUpdate(GroupObject groupObject) {
        try {
            groupObject.setModifiedAccountId(AuthUtils.getCurrentAccount().getOrgId());
            groupObject.setModifiedDatetime(DateTime.now());
            dataAPIClient.patch("group/{id}", groupObject, groupObject.getId());
        } catch (NotFoundException ex) {
            throw new NotFoundException("group not found");
        }
    }

    public void deleteGroupById(String id) {
        dataAPIClient.delete("group/{id}", id);
    }

    public void deleteGroupAndAllRelatedById(String groupId) {
        dataAPIClient.delete("accountgroup?group_id={group_id}", groupId);
        permissionAllocationDomain.deletePermissionAllocationsByGroupId(groupId);
        deleteGroupById(groupId);
    }


}
