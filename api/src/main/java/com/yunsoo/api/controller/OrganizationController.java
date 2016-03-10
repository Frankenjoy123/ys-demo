package com.yunsoo.api.controller;

import com.yunsoo.api.domain.OrganizationDomain;
import com.yunsoo.api.dto.Brand;
import com.yunsoo.api.dto.Organization;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.BrandObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  : Zhe
 * Created on  : 2015/4/2
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/organization")
public class OrganizationController {

    @Autowired
    private OrganizationDomain organizationDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Organization getById(@PathVariable(value = "id") String orgId) {
        orgId = fixOrgId(orgId);
        OrganizationObject object = organizationDomain.getOrganizationById(orgId);
        if (object == null) {
            throw new NotFoundException("organization not found by [id: " + orgId + "]");
        }
        return new Organization(object);
    }

    @RequestMapping(value = "{id}/approve", method = RequestMethod.PUT)
    public void Approve(@PathVariable(value = "id") String orgId) {
        orgId = fixOrgId(orgId);
        organizationDomain.updateOrganizationStatus(orgId, LookupCodes.OrgStatus.AVAILABLE);
    }

    @RequestMapping(value = "{id}/disable", method = RequestMethod.PUT)
    public void Disable(@PathVariable(value = "id") String orgId) {
        orgId = fixOrgId(orgId);
        organizationDomain.updateOrganizationStatus(orgId, LookupCodes.OrgStatus.DISABLE);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'organization:read')")
    public List<Organization> getByFilter(@RequestParam(value = "name", required = false) String name,
                                          @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                          Pageable pageable,
                                          HttpServletResponse response) {
        List<Organization> organizations;
        if (name != null) {
            OrganizationObject object = organizationDomain.getOrganizationByName(name);
            organizations = new ArrayList<>();
            if (object != null) {
                organizations.add(new Organization(object));
            }
        } else {
            Page<OrganizationObject> organizationPage = organizationDomain.getOrganizationList(pageable);
            if (pageable != null) {
                response.setHeader("Content-Range", organizationPage.toContentRange());
            }
            organizations = organizationPage.map(Organization::new).getContent();
        }
        return organizations;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @PreAuthorize("hasPermission('*', 'filterByOrg', 'organization:create')")
    public Organization create(@RequestBody Organization organization) {
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        OrganizationObject object = organization.toOrganizationObject();
        object.setCreatedAccountId(currentAccountId);
        return new Organization(organizationDomain.createOrganization(object));
    }

    @RequestMapping(value = "/brand", method = RequestMethod.POST)
    @PreAuthorize("hasPermission('*', 'filterByOrg', 'organization:create')")
    public Brand createBrand(@RequestBody Brand brand) {
        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        BrandObject object = brand.toBrand(brand);
        object.setCreatedAccountId(currentAccountId);
        return new Brand(organizationDomain.createBrand(object));
    }

    @RequestMapping(value = "/{id}/brand", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('*', 'filterByOrg', 'organization:create')")
    public  List<Brand> filterOrgBrand(@PathVariable(value = "id") String id,
                                       @RequestParam(value="status", required = false)String status,
                                       @RequestParam(value = "name", required = false) String name,
                                       @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable,
                                       HttpServletResponse response) {
        Page<BrandObject> brandPage = organizationDomain.getOrgBrandList(id, name, status, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", brandPage.toContentRange());
        }

        return brandPage.map(Brand::new).getContent();
    }

    @RequestMapping(value = "{id}/logo/{imageName}", method = RequestMethod.GET)
    public ResponseEntity<?> getLogo(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "imageName") String imageName) {
        ResourceInputStream resourceInputStream = organizationDomain.getLogoImage(id, imageName);
        if (resourceInputStream == null) {
            throw new NotFoundException("logo not found");
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        if (resourceInputStream.getContentLength() > 0) {
            builder.contentLength(resourceInputStream.getContentLength());
        }
        return builder.body(new InputStreamResource(resourceInputStream));
    }

    @RequestMapping(value = "{id}/brand", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#orgId, 'orgId', 'organization:modify')")
    public void saveBrandAttachment(@PathVariable(value = "id") String orgId,
                            @RequestBody byte[] attachment) {
        if (attachment != null && attachment.length > 0) {
            orgId = fixOrgId(orgId);
            organizationDomain.saveBrandAttachment(orgId, attachment, "");
        }
    }

    @RequestMapping(value = "{id}/logo", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#orgId, 'orgId', 'organization:modify')")
    public void saveOrgLogo(@PathVariable(value = "id") String orgId,
                            @RequestBody byte[] imageDataBytes) {
        if (imageDataBytes != null && imageDataBytes.length > 0) {
            orgId = fixOrgId(orgId);
            organizationDomain.saveOrgLogo(orgId, imageDataBytes);
        }
    }



    private String fixOrgId(String orgId) {
        if (orgId == null || "current".equals(orgId)) {
            //current orgId
            return tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return orgId;
    }
}
