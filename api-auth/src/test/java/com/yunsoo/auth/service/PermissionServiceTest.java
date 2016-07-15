package com.yunsoo.auth.service;

import com.yunsoo.auth.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by:   Lijian
 * Created on:   2016-07-11
 * Descriptions:
 */
public class PermissionServiceTest extends TestBase {

    @Autowired
    private PermissionService permissionService;

    @Test
    public void test_getPermissionResources() {
        permissionService.getPermissionResources();
    }

}
