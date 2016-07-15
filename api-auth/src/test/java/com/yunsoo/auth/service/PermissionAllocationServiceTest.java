package com.yunsoo.auth.service;

import com.yunsoo.auth.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by:   Lijian
 * Created on:   2016-07-11
 * Descriptions:
 */
public class PermissionAllocationServiceTest extends TestBase {

    @Autowired
    private PermissionAllocationService permissionAllocationService;

    @Test
    public void test_getAllPermissionAllocationsByAccountId() {
        String accountId = "2kadmvn8uh248k5k7wa";
        permissionAllocationService.getAllPermissionAllocationsByAccountId(accountId);
    }
}
