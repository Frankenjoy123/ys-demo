package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.domain.OrganizationDomain;
import com.yunsoo.api.dto.Brand;
import com.yunsoo.api.dto.Organization;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.BrandObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private OrganizationDomain organizationDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private AccountDomain accountDomain;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Organization getById(@PathVariable(value = "id") String orgId) {
        orgId = fixOrgId(orgId);
        OrganizationObject object = organizationDomain.getOrganizationById(orgId);
        if (object == null) {
            throw new NotFoundException("organization not found by [id: " + orgId + "]");
        }
        return new Organization(object);
    }

    @RequestMapping(value = "{id}/disable", method = RequestMethod.PUT)
    public void Disable(@PathVariable(value = "id") String orgId) {
        organizationDomain.updateOrganizationStatus(orgId, LookupCodes.OrgStatus.DISABLE);



    }

    @RequestMapping(value = "{id}/enable", method = RequestMethod.PUT)
    public void Enable(@PathVariable(value = "id") String orgId) {
        organizationDomain.updateOrganizationStatus(orgId, LookupCodes.OrgStatus.AVAILABLE);
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
        object.setTypeCode(LookupCodes.OrgType.MANUFACTURER);
        object.setStatusCode(LookupCodes.OrgStatus.AVAILABLE);
        Brand returnObj = new Brand(organizationDomain.createBrand(object));

        AccountObject accountObject = new AccountObject();
        accountObject.setEmail(returnObj.getEmail());
        accountObject.setIdentifier("admin");
        accountObject.setFirstName(returnObj.getContactName());
        accountObject.setLastName(returnObj.getName());
        accountObject.setPassword("admin");
        accountObject.setPhone(returnObj.getContactMobile());
        accountObject.setOrgId(returnObj.getId());
        accountObject.setCreatedAccountId(currentAccountId);
        accountDomain.createAccount(accountObject);
        return returnObj;
    }


    @RequestMapping(value = "/brand/{id}", method = RequestMethod.GET)
    public Brand getBrandById(@PathVariable(value = "id") String id) {
        BrandObject object = organizationDomain.getBrandById(id);
        if (object == null) {
            throw new NotFoundException("Brand not found by [id: " + id + "]");
        }
        return new Brand(object);
    }

    @RequestMapping(value = "/{id}/brand", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(returnObject, 'organization:read')")
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

    @RequestMapping(value = "{id}/brand/attachment", method = RequestMethod.POST )
    //@PreAuthorize("hasPermission(#orgId, 'orgId', 'organization:modify')")
    public void saveBrandAttachment(@PathVariable(value = "id") String orgId,
                                    @RequestBody Object attachment, MultipartHttpServletRequest request) {
        log.info(request.getFileNames());
        log.info(attachment);
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
