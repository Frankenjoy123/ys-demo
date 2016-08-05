package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.*;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by min on 8/4/16.
 */
public class PermissionControllerTest extends TestBase {

    @Test
    public void testGetPermissions() throws Exception {
        List<PermissionEntry> list = restClient.get("permission", new ParameterizedTypeReference<List<PermissionEntry>>() {
        });
        PermissionEntry entry = list.get(0);
        System.out.println("effect: " + entry.getEffect()+"\n"+"permission: " + entry.getPermission()+"\n"+
                           "restriction: "+entry.getRestriction()+"\n"+"principal: "+entry.getPrincipal());
    }

    @Test
    public void testCheckPermission() throws Exception {
        PermissionCheckRequest request = new PermissionCheckRequest();
        request.setPermission("*:*");
        request.setRestriction("org/*");
        Boolean ok = restClient.post("permission/check", request, Boolean.class);
        assertEquals(ok, true);
    }

    @Test
    public void testCheckPermission_restrictionSubString() throws Exception {
        PermissionCheckRequest request = new PermissionCheckRequest();
        request.setPermission("*:*");
        request.setRestriction("org/");
        Boolean ok = restClient.post("permission/check", request, Boolean.class);
        assertEquals(ok, true);
    }

    @Test
    public void testCheckPermission_restrictionSubSubString() throws Exception {
        PermissionCheckRequest request = new PermissionCheckRequest();
        request.setPermission("*:*");
        request.setRestriction("org");
        Boolean ok = restClient.post("permission/check", request, Boolean.class);
        assertEquals(ok, false);
    }

    @Test
    public void testCheckPermission_permissionSubString() throws Exception {
        PermissionCheckRequest request = new PermissionCheckRequest();
        request.setPermission("*:");
        request.setRestriction("org/*");
        Boolean ok = restClient.post("permission/check", request, Boolean.class);
        assertEquals(ok, true);
    }

    @Test
    public void testCheckPermission_permissionSubSubString() throws Exception {
        PermissionCheckRequest request = new PermissionCheckRequest();
        request.setPermission("*");
        request.setRestriction("org/*");
        Boolean ok = restClient.post("permission/check", request, Boolean.class);
        assertEquals(ok, false);
    }

    @Test
    public void testCheckPermission_emptyCheckRequest() throws Exception {
        PermissionCheckRequest request = new PermissionCheckRequest();
        Boolean ok = restClient.post("permission/check", request, Boolean.class);
        assertEquals(ok, false);
    }

    @Test
    public void testGetResources() throws Exception {
        List<PermissionResource> list = restClient.get("permission/resource", new ParameterizedTypeReference<List<PermissionResource>>() {
        });
        PermissionResource resource = list.get(0);
        System.out.println("code: " + resource.getCode()+"\n"+"name: " + resource.getName()+"\n"+
                "actions: "+resource.getActions()+"\n"+"description: "+resource.getDescription());
    }

    @Test
    public void testGetActions() throws Exception {
        List<PermissionAction> list = restClient.get("permission/action", new ParameterizedTypeReference<List<PermissionAction>>() {
        });
        PermissionAction action = list.get(0);
        System.out.println("code: " + action.getCode()+"\n"+"name: " + action.getName()+"\n"+
                "description: "+action.getDescription());
    }

    @Test
    public void testGetAllPolicies() throws Exception {
        List<PermissionPolicy> list = restClient.get("permission/policy", new ParameterizedTypeReference<List<PermissionPolicy>>() {
        });
        PermissionPolicy policy = list.get(0);
        System.out.println("code: " + policy.getCode()+"\n"+"name: " + policy.getName()+"\n"+
                "description: "+policy.getDescription()+"\n"+"permissions: " + policy.getPermissions());
    }
}