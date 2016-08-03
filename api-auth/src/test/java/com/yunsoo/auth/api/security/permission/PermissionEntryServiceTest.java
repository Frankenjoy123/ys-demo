package com.yunsoo.auth.api.security.permission;

import com.yunsoo.auth.TestBase;
import com.yunsoo.common.web.security.permission.PermissionEntry;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-03-23
 * Descriptions:
 */
public class PermissionEntryServiceTest extends TestBase {

    @Autowired
    private PermissionEntryService permissionEntryService;

    @Test
    public void test_getPermissionEntryByAccountId() {
        String accountId = "2kadmvn8uh248k5k7wa";
        List<PermissionEntry> permissions = permissionEntryService.getExpendedPermissionEntriesByAccountId(accountId);
        permissions.forEach(System.out::println);

    }
}
