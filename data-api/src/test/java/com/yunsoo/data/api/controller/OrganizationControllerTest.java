package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.data.api.Constants;
import com.yunsoo.data.api.ControllerTestBase;
import org.junit.Test;

/**
 * Created by:   Lijian
 * Created on:   2015/9/22
 * Descriptions:
 */
public class OrganizationControllerTest extends ControllerTestBase {

    @Test
    public void test_YunsuOrg() {
        OrganizationObject organizationObject = dataApiClient.get("organization/{id}", OrganizationObject.class, Constants.Ids.YUNSU_ORG_ID);
        assert organizationObject.getId().equals(Constants.Ids.YUNSU_ORG_ID);

    }
}
