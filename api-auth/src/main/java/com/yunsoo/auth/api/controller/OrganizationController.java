package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.api.util.PageUtils;
import com.yunsoo.auth.dto.Organization;
import com.yunsoo.auth.service.OrganizationService;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-07-07
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'organization:read')")
    public Organization getById(@PathVariable(value = "id") String orgId) {
        orgId = AuthUtils.fixOrgId(orgId);
        Organization organization = organizationService.getById(orgId);
        if (organization == null) {
            throw new NotFoundException("organization not found by [id: " + orgId + "]");
        }
        return organization;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'organization:read')")
    public List<Organization> getByFilter(@RequestParam(value = "name", required = false) String name,
                                          @RequestParam(value = "ids_in", required = false) List<String> idsIn,
                                          @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                          Pageable pageable,
                                          HttpServletResponse response) {
        List<Organization> organizations;
        if (name != null) {
            Organization org = organizationService.getByName(name);
            organizations = new ArrayList<>();
            if (org != null) {
                organizations.add(org);
            }
        } else if (idsIn != null) {
            Page<Organization> page = organizationService.getByIds(idsIn, pageable);
            organizations = PageUtils.response(response, page, pageable != null);
        } else {
            Page<Organization> page = organizationService.getAll(pageable);
            organizations = PageUtils.response(response, page, pageable != null);
        }
        return organizations;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @PreAuthorize("hasPermission('*', 'org', 'organization:create')")
    public Organization create(@RequestBody @Valid Organization org) {
        return organizationService.create(org);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization:write')")
    public void patchUpdate(@PathVariable(value = "id") String orgId,
                            @RequestBody Organization org) {
        org.setId(orgId);
        organizationService.patchUpdate(org);
    }

    @RequestMapping(value = "{id}/disable", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization:write')")
    public void disable(@PathVariable(value = "id") String orgId) {
        organizationService.updateStatus(orgId, Constants.OrgStatus.DISABLED);
    }

    @RequestMapping(value = "{id}/enable", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization:write')")
    public void enable(@PathVariable(value = "id") String orgId) {
        organizationService.updateStatus(orgId, Constants.OrgStatus.AVAILABLE);
    }

}