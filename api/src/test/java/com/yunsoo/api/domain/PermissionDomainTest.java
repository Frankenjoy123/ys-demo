package com.yunsoo.api.domain;

import com.yunsoo.api.config.DomainConfiguration;
import com.yunsoo.api.object.TPermission;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {DomainConfiguration.class})
public class PermissionDomainTest {

    @Autowired
    private PermissionDomain permissionDomain;

    @Test
    public void test_getPermissionPolicies() {
        assert permissionDomain.getPermissionPolicies().size() > 0;
    }

    @Test
    public void test_getAccountPermissionsByAccountId() {
        List<TPermission> permissions = permissionDomain.getAccountPermissionsByAccountId("2kadmvn8uh248k5k7wa");
        System.out.println(permissions);
        assert permissions.size() > 0;
    }
}
