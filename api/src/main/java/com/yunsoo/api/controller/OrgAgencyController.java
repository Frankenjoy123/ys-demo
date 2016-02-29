package com.yunsoo.api.controller;

import com.yunsoo.api.domain.OrgAgencyDomain;
import com.yunsoo.api.dto.Location;
import com.yunsoo.api.dto.OrgAgency;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.OrgAgencyObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Haitao
 * Created on  : 2015/9/2
 * Descriptions:
 */

@RestController
@RequestMapping(value = "/organizationagency")
public class OrgAgencyController {

    @Autowired
    private OrgAgencyDomain orgAgencyDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    //query by id
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'orgagency:read')")
    public OrgAgency getById(@PathVariable(value = "id") String id) {
        OrgAgencyObject orgAgencyObject = findOrgAgencyById(id);
        if (orgAgencyObject == null) {
            throw new NotFoundException("organization agency not found");
        }
        OrgAgency orgAgency = new OrgAgency(orgAgencyObject);
        return orgAgency;
    }

    //query by org id
    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'filterByOrg', 'orgagency:read')")
    public List<OrgAgency> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                       Pageable pageable,
                                       HttpServletResponse response) {
        orgId = fixOrgId(orgId);
        Page<OrgAgencyObject> orgAgencyPage = orgAgencyDomain.getOrgAgencyByOrgId(orgId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", orgAgencyPage.toContentRange());
        }
        List<OrgAgency> orgAgencys = orgAgencyPage.map(p -> {
            OrgAgency orgAgency = new OrgAgency(p);
            return orgAgency;
        }).getContent();

        return orgAgencys;
    }

    //query locations
    @RequestMapping(value = "location", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'filterByOrg', 'orgagency:read')")
    public List<Location> getLocationsByFilter(@RequestParam(value = "parent_id", required = false) String parentId) {
        return orgAgencyDomain.getLocationsByFilter(parentId).stream().map(Location::new).collect(Collectors.toList());
    }

    //create organization agency
    @RequestMapping(value = "", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#orgagency, 'orgagency:create')")
    public OrgAgency create(@RequestBody OrgAgency orgAgency) {
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        OrgAgencyObject orgAgencyobject = orgAgency.toOrgAgencyObject();
        orgAgencyobject.setCreatedAccountId(currentAccountId);
        orgAgencyobject.setCreatedDateTime(DateTime.now());
        orgAgencyobject.setStatusCode(LookupCodes.OrgAgencyStatus.ACTIVATED);
        if (StringUtils.hasText(orgAgency.getOrgId()))
            orgAgencyobject.setOrgId(orgAgency.getOrgId());
        else
            orgAgencyobject.setOrgId(tokenAuthenticationService.getAuthentication().getDetails().getOrgId());

        return new OrgAgency(orgAgencyDomain.createOrgAgency(orgAgencyobject));
    }

    //update organization agency
    @PreAuthorize("hasPermission(#orgagency, 'orgagency:modify')")
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void update(@PathVariable("id") String id, @RequestBody OrgAgency orgAgency) {
        OrgAgencyObject orgAgencyObject = findOrgAgencyById(id);
        orgAgencyObject.setName(orgAgency.getName());
        orgAgencyObject.setLocationIds(orgAgency.getLocationIds());
        orgAgencyObject.setAddress(orgAgency.getAddress());
        orgAgencyObject.setDescription(orgAgency.getDescription());
        orgAgencyObject.setParentId(orgAgency.getParentId());
        orgAgencyObject.setModifiedAccountId(tokenAuthenticationService.getAuthentication().getDetails().getOrgId());
        orgAgencyObject.setModifiedDateTime(DateTime.now());
        orgAgencyDomain.updateOrgAgency(orgAgencyObject);
    }


    //delete organization agency
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {
        OrgAgencyObject orgAgencyObject = orgAgencyDomain.getOrgAgencyById(id);
        if (orgAgencyObject != null) {
            if (LookupCodes.OrgAgencyStatus.DEACTIVATED.equals(orgAgencyObject.getStatusCode())) {
                throw new UnprocessableEntityException("illegal operation");
            }
            orgAgencyDomain.deleteOrgAgency(id);
        }
    }


    private OrgAgencyObject findOrgAgencyById(String id) {
        OrgAgencyObject orgAgencyObject = orgAgencyDomain.getOrgAgencyById(id);
        if (orgAgencyObject == null || LookupCodes.OrgAgencyStatus.DEACTIVATED.equals(orgAgencyObject.getStatusCode())) {
            throw new NotFoundException("organization agency not found");
        }
        return orgAgencyObject;
    }

    private String fixOrgId(String orgId) {
        if (orgId == null || "current".equals(orgId)) {
            //current orgId
            return tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return orgId;
    }
}
