package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.Organization;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.RestErrorResultException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by min on 8/3/16.
 */
public class OrganizationControllerTest extends TestBase {

    private static List<String> orgIds;
    private String orgId;

    @Before
    public void createOrganizations() {

        Organization org = restClient.post("organization", createOrg("TestOrg"), Organization.class);
        orgId = org.getId();

        if (orgIds == null) {
            orgIds = batchCreateOrg();
        }
    }

    private Organization createOrg(String orgName) {
        Organization org = new Organization();
        org.setName(orgName);
        org.setDescription("Test organization is a great company!");
        org.setCreatedDateTime(DateTime.now());
        org.setTypeCode(Constants.OrgType.CARRIER);
        return org;
    }

    private List<String> batchCreateOrg() {
        return IntStream.range(0, 4).parallel().mapToObj(i -> {
            return restClient.postAsync("organization", createOrg("TestOrg" + i), Organization.class);
        }).map(f -> {
                    try {
                        Organization a = f.get();
                        System.out.println(a + " " + a.getName());
                        return a.getId();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
        ).collect(Collectors.toList());
    }

    private Organization getById(String id) {
        return restClient.get("organization/{id}", Organization.class, id);
    }

    private void disableOrg(String id) {
        restClient.post("organization/{id}/disable", null, null, id);
    }

    private void enableOrg(String id) {
        restClient.post("organization/{id}/enable", null, null, id);
    }

    @Test
    public void testGetById() throws Exception {
        Organization org = getById(orgId);
        assertEquals(org.getName(), "TestOrg");
    }

    @Test(expected = NotFoundException.class)
    public void testGetById_404_notExistedId() {
        getById(orgId + "xx");
    }

    @Test(expected = HttpMessageNotReadableException.class)
    public void testGetById_404_emptyId() {
        getById("");
    }

    @Test(expected = NotFoundException.class)
    public void testGetById_404_idSubString() {
        getById(orgId.substring(0, orgId.length() - 2));
    }

    @Test
    public void testGetByFilter() throws Exception {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("name", "TestOrg2")
                .append("ids_in", orgIds)
                .build();
        List<Organization> list = restClient.get("organization" + query, new ParameterizedTypeReference<List<Organization>>() {
        });
        assert list.size() == 1;
    }

    @Test
    public void testGetByFilter_notInIdsIn() throws Exception {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("name", "TestOrg100")
                .append("ids_in", orgIds)
                .build();
        List<Organization> list = restClient.get("organization" + query, new ParameterizedTypeReference<List<Organization>>() {
        });
        assert list.size() == 0;
    }

    @Test
    public void testGetByFilter_name() throws Exception {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("name", "TestOrg")
                .build();
        List<Organization> list = restClient.get("organization" + query, new ParameterizedTypeReference<List<Organization>>() {
        });
        assert list.size() == 1;
    }

    @Test
    public void testGetByFilter_notExistedName() throws Exception {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("name", "TestOrg100")
                .build();
        List<Organization> list = restClient.get("organization" + query, new ParameterizedTypeReference<List<Organization>>() {
        });
        assert list.size() == 0;
    }

    @Test
    public void testGetByFilter_idsIn() throws Exception {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("ids_in", orgIds)
                .build();
        List<Organization> list = restClient.get("organization" + query, new ParameterizedTypeReference<List<Organization>>() {
        });
        assert list.size() == 4;
    }

    @Test
    public void testGetByFilter_noParameter() throws Exception {
        List<Organization> list = restClient.get("organization", new ParameterizedTypeReference<List<Organization>>() {
        });
        assert list.size() > 2;
    }

    @Test
    public void testGetByFilter_pageable() throws Exception {
        Pageable pageable = new PageRequest(0, 2);

        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append(pageable)
                .build();
        List<Organization> list = restClient.get("organization" + query, new ParameterizedTypeReference<List<Organization>>() {
        });
        assert list.size() == 2;
    }

    @Test
    public void testCreate() throws Exception {
        Organization org = restClient.post("organization", createOrg("TestOrg"), Organization.class);
        org = getById(org.getId());
        assertEquals(org.getName(), "TestOrg");
    }

    @Test
    public void testCreate_wrongTypeCode() throws Exception {
        Organization org = createOrg("TestOrg");
        org.setTypeCode("random type");
        org = restClient.post("organization", org, Organization.class);
        assertEquals(getById(org.getId()).getTypeCode(), "random type");
    }

    @Test(expected = BadRequestException.class)
    public void testCreate_404_emptyTypeCode() throws Exception {
        Organization org = createOrg("TestOrg");
        org.setTypeCode("");
        restClient.post("organization", org, Organization.class);
    }

    @Test(expected = BadRequestException.class)
    public void testCreate_404_nullTypeCode() throws Exception {
        Organization org = createOrg("TestOrg");
        org.setTypeCode(null);
        restClient.post("organization", org, Organization.class);
    }

    @Test(expected = BadRequestException.class)
    public void testCreate_404_emptyName() throws Exception {
        Organization org = createOrg("TestOrg");
        org.setName("");
        restClient.post("organization", org, Organization.class);
    }

    @Test(expected = BadRequestException.class)
    public void testCreate_404_nullName() throws Exception {
        Organization org = createOrg("TestOrg");
        org.setName(null);
        restClient.post("organization", org, Organization.class);
    }

    @Test
    public void testPatchUpdate_name() throws Exception {
        Organization org = getById(orgId);
        assertEquals(org.getName(), "TestOrg");
        org.setName("TestOrg2222");
        restClient.patch("organization/{id}", org, orgId);
        assertEquals(getById(orgId).getName(), "TestOrg2222");
    }

    @Test
    public void testPatchUpdate_nullName() throws Exception {
        Organization org = createOrg(null);
        restClient.patch("organization/{id}", org, orgId);
        org = getById(orgId);
        assertEquals(org.getName(), "TestOrg");
    }

    @Test
    public void testPatchUpdate_typeCode() throws Exception {
        Organization org = getById(orgId);
        assertEquals(org.getTypeCode(), Constants.OrgType.CARRIER);
        org.setTypeCode(Constants.OrgType.BRAND);
        restClient.patch("organization/{id}", org, orgId);
        assertEquals(getById(orgId).getTypeCode(), Constants.OrgType.CARRIER);
    }

    @Test
    public void testPatchUpdate_nothingChanged() throws Exception {
        Organization org = getById(orgId);
        restClient.patch("organization/{id}", org, orgId);
        assertEquals(getById(orgId).getTypeCode(), Constants.OrgType.CARRIER);
    }

    @Test
    public void testDisable() throws Exception {
        assertEquals(getById(orgId).getStatusCode(), Constants.OrgStatus.AVAILABLE);
        disableOrg(orgId);
        assertEquals(getById(orgId).getStatusCode(), Constants.OrgStatus.DISABLED);
    }

    @Test
    public void testDisable_notExistedId() throws Exception {
        disableOrg(orgId.substring(0, orgId.length() - 2));
    }

    @Test(expected = RestErrorResultException.class)
    public void testDisable_nullId() throws Exception {
        disableOrg(null);
    }

    @Test(expected = RestErrorResultException.class)
    public void testDisable_emptyId() throws Exception {
        disableOrg("");
    }

    @Test
    public void testEnable() throws Exception {
        disableOrg(orgId);
        assertEquals(getById(orgId).getStatusCode(), Constants.OrgStatus.DISABLED);
        restClient.post("organization/{id}/enable", null, null, orgId);
        assertEquals(getById(orgId).getStatusCode(), Constants.OrgStatus.AVAILABLE);
    }

    @Test(expected = RestErrorResultException.class)
    public void testEnable_nullId() throws Exception {
        enableOrg(null);
    }

    @Test(expected = RestErrorResultException.class)
    public void testEnable_emptyId() throws Exception {
        enableOrg("");
    }
}