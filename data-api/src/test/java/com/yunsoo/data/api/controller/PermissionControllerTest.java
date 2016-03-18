package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.PermissionActionObject;
import com.yunsoo.common.data.object.PermissionRegionObject;
import com.yunsoo.common.data.object.PermissionResourceObject;
import com.yunsoo.data.api.Constants;
import com.yunsoo.data.api.ControllerTestBase;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Arrays;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-03-18
 * Descriptions:
 */
public class PermissionControllerTest extends ControllerTestBase {

    @Test
    public void test_PermissionResource() {
        List<PermissionResourceObject> resourceObjects = dataAPIClient.get("permission/resource", new ParameterizedTypeReference<List<PermissionResourceObject>>() {
        });
        assert resourceObjects.size() > 0;
    }

    @Test
    public void test_PermissionAction() {
        List<PermissionActionObject> actionObjects = dataAPIClient.get("permission/action", new ParameterizedTypeReference<List<PermissionActionObject>>() {
        });
        assert actionObjects.size() > 0;
    }

    @Test
    public void test_PermissionRegion() {
        String orgId = Constants.Ids.YUNSU_ORG_ID;

        PermissionRegionObject obj = new PermissionRegionObject();
        obj.setOrgId(orgId);
        obj.setName("测试权限范围");
        obj.setRestrictions(Arrays.asList("org/" + orgId, "org/" + orgId));
        obj.setTypeCode("default");

        obj = dataAPIClient.post("permission/region", obj, PermissionRegionObject.class);
        assert obj != null && obj.getId() != null;

        obj = dataAPIClient.get("permission/region/{id}", PermissionRegionObject.class, obj.getId());
        assert obj != null;

        List<PermissionRegionObject> permissionRegionObjects = dataAPIClient.get("permission/region?org_id={p}", new ParameterizedTypeReference<List<PermissionRegionObject>>() {
        }, orgId);
        assert permissionRegionObjects.size() > 0;

        dataAPIClient.delete("permission/region/{id}", obj.getId());

    }


}
