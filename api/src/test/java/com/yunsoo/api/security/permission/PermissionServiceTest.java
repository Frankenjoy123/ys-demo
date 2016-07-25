package com.yunsoo.api.security.permission;

import com.yunsoo.api.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-03-23
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebIntegrationTest("server.port=0")
public class PermissionServiceTest {

    @Autowired
    private PermissionService permissionService;

    @Test
    public void test_getPermissionEntryByAccountId() {
        String accountId = "2kadmvn8uh248k5k7wa";
        List<PermissionEntry> permissions = permissionService.getExpendedPermissionEntriesByAccountId(accountId);
        permissions.forEach(System.out::println);

    }
}
