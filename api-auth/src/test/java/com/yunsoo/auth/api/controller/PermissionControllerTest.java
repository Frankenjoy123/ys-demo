package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.PermissionEntry;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Created by min on 8/4/16.
 */
public class PermissionControllerTest extends TestBase {

    @Test
    public void testGetPermissions() throws Exception {
        List<PermissionEntry> list = restClient.get("permission", new ParameterizedTypeReference<List<PermissionEntry>>() {
        });
        System.out.println("list is "+list);
    }

    @Test
    public void testCheckPermission() throws Exception {

    }

    @Test
    public void testGetResources() throws Exception {

    }

    @Test
    public void testGetActions() throws Exception {

    }

    @Test
    public void testGetAllPolicies() throws Exception {

    }
}