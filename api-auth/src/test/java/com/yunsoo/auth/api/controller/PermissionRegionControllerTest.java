package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.PermissionRegion;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by min on 8/5/16.
 */
public class PermissionRegionControllerTest extends TestBase {

    private static List<String> regionIds;
    private String regionId;
    
    @Before
    public void createPermissionRegions() {

        PermissionRegion region = restClient.post("permission/region", createRegion("TestRegion"), PermissionRegion.class);
        regionId = region.getId();

        if (regionIds == null) {
            regionIds = batchCreateRegion();
        }
    }

    private PermissionRegion createRegion(String name) {
        PermissionRegion region = new PermissionRegion();
        region.setOrgId(YUNSU_ORG_ID);
        region.setName(name);
        region.setDescription("region description");
        region.setTypeCode(Constants.PermissionRegionType.CUSTOM);
        return region;
    }

    private List<String> batchCreateRegion() {
        return IntStream.range(0, 4).parallel().mapToObj(i -> {
            return restClient.postAsync("permission/region", createRegion("TestRegion" + i), PermissionRegion.class);
        }).map(f -> {
                    try {
                        PermissionRegion a = f.get();
                        System.out.println(a + " " + a.getName());
                        return a.getId();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
        ).collect(Collectors.toList());
    }

    private PermissionRegion getById(String id) {
        return restClient.get("permission/region/{id}", PermissionRegion.class, id);
    }
    
    @Test
    public void testGetById() throws Exception {
        PermissionRegion region = getById(regionId);
        assertEquals(region.getName(), "TestRegion");
    }

    @Test
    public void testGetDefaultByRegionId() throws Exception {
        PermissionRegion region = restClient.get("permission/region/default", PermissionRegion.class);
        assertEquals(region.getName(), "Default Region");
    }

    @Test
    public void testGetList() throws Exception {
        List<PermissionRegion> list = restClient.get("permission/region", new ParameterizedTypeReference<List<PermissionRegion>>() {
        });
        System.out.println("list is "+list);
    }

    @Test
    public void testCreate() throws Exception {
        PermissionRegion region = createRegion("TestRegionCreate");
        List<String> list = Arrays.asList("restriction1", "restriction2", "restriction3");;
        region.setRestrictions(list);
        region = restClient.post("permission/region", region, PermissionRegion.class);
        assertEquals(region.getName(), "TestRegionCreate");
    }

    @Test
    public void testCreate_400_wrongTypeCode() throws Exception {
        PermissionRegion region = createRegion("TestRegion");
        region.setTypeCode("random type");
        region = restClient.post("permission/region", region, PermissionRegion.class);
        assertEquals(getById(region.getId()).getTypeCode(), Constants.PermissionRegionType.CUSTOM);
    }

    @Test(expected = BadRequestException.class)
    public void testCreate_404_emptyTypeCode() throws Exception {
        PermissionRegion region = createRegion("TestRegion");
        region.setTypeCode("");
        restClient.post("permission/region", region, PermissionRegion.class);
    }

    @Test(expected = BadRequestException.class)
    public void testCreate_404_nullTypeCode() throws Exception {
        PermissionRegion region = createRegion("TestRegion");
        region.setTypeCode(null);
        restClient.post("permission/region", region, PermissionRegion.class);
    }

    @Test(expected = BadRequestException.class)
    public void testCreate_404_emptyName() throws Exception {
        PermissionRegion region = createRegion("TestRegion");
        region.setName("");
        restClient.post("permission/region", region, PermissionRegion.class);
    }

    @Test(expected = BadRequestException.class)
    public void testCreate_404_nullName() throws Exception {
        PermissionRegion region = createRegion("TestRegion");
        region.setName(null);
        restClient.post("permission/region", region, PermissionRegion.class);
    }

    @Test
    public void testAddRestrictionsToRegion() throws Exception {
        List<String> list = Arrays.asList("restriction1", "restriction2", "restriction3");
        String[] results = restClient.post("permission/region/{id}/restrictions", list, String[].class, regionId);
        assertEquals(results[0], "restriction1");
    }

    @Test
    public void testAddRestrictionsToDefaultRegion() throws Exception {
        List<String> list = Arrays.asList("restriction1", "restriction2", "restriction3");
        String[] results = restClient.post("permission/region/default/restrictions", list, String[].class);
        assertEquals(results[0], "restriction1");
    }

    @Test
    public void testPatchUpdate() throws Exception {
        PermissionRegion region = getById(regionId);
        assertEquals(region.getDescription(), "region description");
        List<String> list = Arrays.asList("restriction1", "restriction2", "restriction3");
        region.setRestrictions(list);
        region.setDescription("region description changed");
        restClient.patch("permission/region/{id}", region, regionId);
        assertEquals(getById(regionId).getDescription(), "region description changed");
        assertEquals(getById(regionId).getRestrictions(), list);
    }

    @Test
    public void testPatchUpdate_name() throws Exception {
        PermissionRegion region = getById(regionId);
        assertEquals(region.getName(), "TestRegion");
        region.setName("TestRegion 2222");
        restClient.patch("permission/region/{id}", region, regionId);
        assertEquals(getById(regionId).getName(), "TestRegion 2222");
    }

    @Test
    public void testPatchUpdate_nullName() throws Exception {
        PermissionRegion region = createRegion(null);
        restClient.patch("permission/region/{id}", region, regionId);
        region = getById(regionId);
        assertEquals(region.getName(), "TestRegion");
    }

    @Test
    public void testPatchUpdate_typeCode() throws Exception {
        PermissionRegion region = getById(regionId);
        assertEquals(region.getTypeCode(), Constants.PermissionRegionType.CUSTOM);
        region.setTypeCode(Constants.PermissionRegionType.DEFAULT);
        restClient.patch("permission/region/{id}", region, regionId);
        assertEquals(getById(regionId).getTypeCode(), Constants.PermissionRegionType.CUSTOM);
    }

    @Test
    public void testPatchUpdate_nothingChanged() throws Exception {
        PermissionRegion region = getById(regionId);
        restClient.patch("permission/region/{id}", region, regionId);
        assertEquals(getById(regionId).getTypeCode(), Constants.PermissionRegionType.CUSTOM);
    }

    @Test(expected = NotFoundException.class)
    public void testDelete() throws Exception {
        assertEquals(getById(regionId).getTypeCode(), Constants.PermissionRegionType.CUSTOM);
        restClient.delete("permission/region/{id}", regionId);
        getById(regionId);
    }

    @Test
    public void testDelete_404_unknownId() throws Exception {
        restClient.delete("permission/region/{id}", "NotExistedId");
    }
}