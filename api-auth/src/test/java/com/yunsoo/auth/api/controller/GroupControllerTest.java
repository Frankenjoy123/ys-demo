package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.api.security.permission.PermissionEntry;
import com.yunsoo.auth.dto.Group;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by min on 7/28/16.
 */
public class GroupControllerTest extends TestBase {

    private Group testGroup;

    @Before
    public void createGroup() {
        Group group = new Group();
        group.setOrgId(YUNSU_ORG_ID);
        group.setName("云溯科技");
        group.setDescription("领先国内外");
        group.setCreatedAccountId(Constants.SYSTEM_ACCOUNT_ID);
        group.setCreatedDateTime(DateTime.now());
        testGroup = restClient.post("group", group, Group.class);
    }

    @Test
    public void testGetById() throws Exception {
        Group group = restClient.get("group/{id}", Group.class, testGroup.getId());
        assertEquals(group.getName(), "云溯科技");
    }

    @Test(expected = NotFoundException.class)
    public void test_getById_404_unknownId() {
        restClient.get("group/{id}", Group.class, "unknownId");
    }

    @Test
    public void testGetByOrgId() throws Exception {
        List<Group> groups = restClient.get("group?org_id={0}", new ParameterizedTypeReference<List<Group>>() {
        }, YUNSU_ORG_ID);
        assert groups.size() > 0;
    }

    @Test
    public void testGetByOrgId_unknownOrg() throws Exception {
        List<Group> groups = restClient.get("group?org_id={0}", new ParameterizedTypeReference<List<Group>>() {
        }, "unknownOrg");
        assert groups.size() == 0;
    }

    @Test
    public void testPatchUpdateGroup() throws Exception {
        Group group = restClient.get("group/{id}", Group.class, testGroup.getId());
        group.setDescription("description changed!");
        restClient.patch("group/{id}", group, testGroup.getId());
        group = restClient.get("group/{id}", Group.class, testGroup.getId());
        assert group.getDescription().equals("description changed!");
    }

    @Test(expected = BadRequestException.class)
    public void test_patchUpdateGroup_400_nullAccount() {
        restClient.patch("group/{id}", null, testGroup.getId());
    }

    @Test(expected = NotFoundException.class)
    public void test_patchUpdateAccount_404_unknownId() {
        Group group = new Group();
        group.setDescription("description changed!");
        restClient.patch("group/{id}", group, "NotExistedId");
    }

    @Test(expected = NotFoundException.class)
    public void testDelete() throws Exception {
        Group group = restClient.get("group/{id}", Group.class, testGroup.getId());
        restClient.delete("group/{id}",group.getId());
        restClient.get("group/{id}", Group.class, testGroup.getId());
    }

    @Test(expected = InternalServerErrorException.class)
    public void testDelete_404_unknownId() throws Exception {
        restClient.delete("group/{id}", "NotExistedId");
    }

    @Test
    public void testGetAllPermissionByGroupId() throws Exception {
        List<PermissionEntry> list = restClient.get("group/{group_id}/permission", new ParameterizedTypeReference<List<PermissionEntry>>() {
        }, testGroup.getId());
        System.out.println("Group permission is "+list);
    }
}