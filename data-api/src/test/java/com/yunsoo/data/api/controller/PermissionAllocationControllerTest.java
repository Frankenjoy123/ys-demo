package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.PermissionAllocationObject;
import com.yunsoo.data.api.Constants;
import com.yunsoo.data.api.ControllerTestBase;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-03-18
 * Descriptions:
 */
public class PermissionAllocationControllerTest extends ControllerTestBase {

    @Test
    public void test_All() {
        String orgId = Constants.Ids.YUNSU_ORG_ID;
        String accountId = Constants.Ids.SYSTEM_ACCOUNT_ID;

        PermissionAllocationObject obj = new PermissionAllocationObject();
        obj.setPrincipal("account/" + accountId);
        obj.setRestriction("org/" + orgId);
        obj.setPermission("account:read");
        obj.setEffect(PermissionAllocationObject.Effect.allow);
        obj.setCreatedAccountId(accountId);

        obj = dataAPIClient.post("permissionAllocation", obj, PermissionAllocationObject.class);
        assert obj != null && obj.getId() != null;

        obj = dataAPIClient.get("permissionAllocation/{id}", PermissionAllocationObject.class, obj.getId());
        assert obj != null && obj.getId() != null;

        List<PermissionAllocationObject> permissionAllocationObjects = dataAPIClient.get("permissionAllocation?principal={p}", new ParameterizedTypeReference<List<PermissionAllocationObject>>() {
        }, "account/" + accountId);
        assert permissionAllocationObjects.size() > 0;

        dataAPIClient.delete("permissionAllocation/{id}", obj.getId());

    }

}
