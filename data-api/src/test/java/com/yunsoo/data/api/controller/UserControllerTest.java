package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.data.api.Constants;
import com.yunsoo.data.api.ControllerTestBase;
import org.junit.Test;

/**
 * Created by:   Lijian
 * Created on:   2015/9/23
 * Descriptions:
 */
public class UserControllerTest extends ControllerTestBase {

    @Test
    public void test_AnonymousUser() {
        UserObject userObject = dataApiClient.get("user/{id}", UserObject.class, Constants.Ids.ANONYMOUS_USER_ID);
        assert userObject.getId().equals(Constants.Ids.ANONYMOUS_USER_ID);

    }
}
