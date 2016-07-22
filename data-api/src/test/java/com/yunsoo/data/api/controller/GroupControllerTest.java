package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.GroupObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.api.Constants;
import com.yunsoo.data.api.ControllerTestBase;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/24
 * Descriptions:
 */
public class GroupControllerTest extends ControllerTestBase {

    @Test
    public void test_All() {
        GroupObject groupObject = new GroupObject();
        groupObject.setName("TestGroupName");
        groupObject.setDescription("TestDescription");
        groupObject.setOrgId(Constants.Ids.YUNSU_ORG_ID);
        groupObject.setCreatedAccountId(Constants.Ids.SYSTEM_ACCOUNT_ID);
        groupObject.setCreatedDateTime(DateTime.now());
        groupObject = dataApiClient.post("group", groupObject, GroupObject.class);
        String id = groupObject.getId();
        System.out.println("group created: [id: " + id + "]");

        groupObject = dataApiClient.get("group/{id}", GroupObject.class, groupObject.getId());

        List<GroupObject> groupObjects = dataApiClient.get("group?org_id={orgId}", new ParameterizedTypeReference<List<GroupObject>>() {
        }, groupObject.getOrgId());
        assert groupObjects.stream().filter(o -> o.getId().equals(id)).count() > 0 : "get by org_id failed";

        groupObject.setName("TestGroupName1");
        dataApiClient.patch("group/{id}", groupObject, id);
        groupObject = dataApiClient.get("group/{id}", GroupObject.class, id);
        assert groupObject.getName().equals("TestGroupName1") : "patch update failed";

        dataApiClient.delete("group/{id}", id);

        try {
            dataApiClient.get("group/{id}", GroupObject.class, id);
            assert false : "delete failed";
        } catch (NotFoundException ex) {
            System.out.println("group deleted: [id: " + id + "]");
        }

    }

}
