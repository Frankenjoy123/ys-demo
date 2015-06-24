package com.yunsoo.api.domain;

import com.yunsoo.api.client.DataAPIClient;
import com.yunsoo.common.data.object.GroupObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

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

    public GroupObject getById(String groupId) {
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
        return dataAPIClient.post("group", groupObject, GroupObject.class);
    }

}
