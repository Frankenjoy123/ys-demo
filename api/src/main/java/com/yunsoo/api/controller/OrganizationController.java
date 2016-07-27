package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.*;
import com.yunsoo.api.dto.Attachment;
import com.yunsoo.api.dto.Brand;
import com.yunsoo.api.dto.ImageRequest;
import com.yunsoo.api.dto.Organization;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.api.util.PageUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.data.object.BrandObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private AccountDomain accountDomain;

    @Autowired
    private BrandDomain brandDomain;

    @Autowired
    private PermissionDomain permissionDomain;

    @Autowired
    private PermissionAllocationDomain permissionAllocationDomain;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'organization:read')")
    public Organization getById(@PathVariable(value = "id") String orgId) {
        orgId = AuthUtils.fixOrgId(orgId);
        OrganizationObject object = organizationDomain.getOrganizationById(orgId);
        if (object == null) {
            throw new NotFoundException("organization not found by [id: " + orgId + "]");
        }
        return new Organization(object);
    }

    @RequestMapping(value = "{id}/disable", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization:write')")
    public void Disable(@PathVariable(value = "id") String orgId) {
        organizationDomain.updateOrganizationStatus(orgId, LookupCodes.OrgStatus.DISABLED);
    }

    @RequestMapping(value = "{id}/enable", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization:write')")
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
            organizations = PageUtils.response(response, organizationPage.map(Organization::new), pageable != null);
        }
        return organizations;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @PreAuthorize("hasPermission('*', 'org', 'organization:create')")
    public Organization create(@RequestBody Organization organization) {
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        OrganizationObject object = organization.toOrganizationObject();
        object.setCreatedAccountId(currentAccountId);
        return new Organization(organizationDomain.createOrganization(object));
    }

    @RequestMapping(value = "/brand", method = RequestMethod.POST)
    @PreAuthorize("hasPermission('current', 'org', 'organization:create')")
    public Brand createBrand(@RequestBody Brand brand) {
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        BrandObject object = brand.toBrand(brand);
        object.setCreatedAccountId(currentAccountId);
        Brand returnObj = new Brand(organizationDomain.createBrand(object));

        permissionDomain.putOrgRestrictionToDefaultPermissionRegion(brand.getCarrierId(), returnObj.getId());

        AccountObject accountObject = new AccountObject();
        accountObject.setEmail(returnObj.getEmail());
        accountObject.setIdentifier("admin");
        accountObject.setFirstName(returnObj.getContactName());
        accountObject.setLastName("");
        accountObject.setPassword("admin");
        accountObject.setPhone(returnObj.getContactMobile());
        accountObject.setOrgId(returnObj.getId());
        accountObject.setCreatedAccountId(Constants.Ids.SYSTEM_ACCOUNT_ID);
        AccountObject createdAccount = accountDomain.createAccount(accountObject, true);
        permissionAllocationDomain.allocateAdminPermissionOnCurrentOrgToAccount(createdAccount.getId());

        return returnObj;
    }

    @RequestMapping(value = "/brand/{id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization:write')")
    public void patchBrand(@PathVariable("id") String id, @RequestBody Brand brand) {
        organizationDomain.patchBrand(id, brand.toBrand(brand));
    }

    @RequestMapping(value = "/brand/{id}", method = RequestMethod.GET)
    public Brand getBrandById(@PathVariable(value = "id") String id, @RequestParam(value = "carrier_info", required = false) boolean includeCarrier) {
        BrandObject object = organizationDomain.getBrandById(id);
        if (object == null) {
            throw new NotFoundException("Brand not found by [id: " + id + "]");
        }

        Brand returnObject = new Brand(object);

        if (StringUtils.hasText(object.getAttachment())) {
            List<AttachmentObject> attachmentObjectList = brandDomain.getAttachmentList(object.getAttachment());
            returnObject.setAttachmentList(attachmentObjectList.stream().map(Attachment::new).collect(Collectors.toList()));
        }

        if (includeCarrier)
            returnObject.setCarrier(new Organization(organizationDomain.getOrganizationById(returnObject.getCarrierId())));

        return returnObject;
    }


    @RequestMapping(value = "/{id}/brand/count", method = RequestMethod.GET)
    public int countBrand(@PathVariable(value = "id") String id) {
        return organizationDomain.countBrand(id, null);
    }

    @RequestMapping(value = "/{id}/brand", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'organization:read')")
    public List<Brand> filterOrgBrand(@PathVariable(value = "id") String id,
                                      @RequestParam(value = "status", required = false) String status,
                                      @RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "search_text", required = false) String searchText,
                                      @RequestParam(value = "category_id", required = false) String categoryId,
                                      @RequestParam(value = "start_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime startTime,
                                      @RequestParam(value = "end_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime endTime,
                                      Pageable pageable,
                                      HttpServletResponse response) {
        if (!StringUtils.hasText(searchText))
            searchText = null;
        Page<BrandObject> brandPage = organizationDomain.getOrgBrandList(id, name, status, searchText, startTime, endTime, categoryId, pageable);
        return PageUtils.response(response, brandPage.map(Brand::new), pageable != null);
    }

    @RequestMapping(value = "/{id}/brand/name", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'organization:read')")
    public List<Brand> filterOrgBrandByName(@PathVariable(value = "id") String id,
                                            @RequestParam(value = "name", required = false) String name,
                                            Pageable pageable,
                                            HttpServletResponse response) {
        if (!StringUtils.hasText(name))
            name = null;
        Page<BrandObject> brandPage = organizationDomain.getOrgBrandListByName(id, name, pageable);
        return PageUtils.response(response, brandPage.map(Brand::new), pageable != null);
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


    @RequestMapping(value = "{id}/logo", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization:write')")
    public void saveOrgLogo(@PathVariable(value = "id") String orgId,
                            @RequestBody byte[] imageDataBytes) {
        if (imageDataBytes != null && imageDataBytes.length > 0) {
            orgId = AuthUtils.fixOrgId(orgId);
            organizationDomain.saveOrgLogo(orgId, imageDataBytes);
        }
    }

    @RequestMapping(value = "{id}/logo/{imageName}", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization_config:write')")
    public void saveCarrierLogo(@PathVariable(value = "id") String orgId, @PathVariable(value = "imageName") String imageName,
                                @RequestBody @Valid ImageRequest imageRequest) {

        String imageData = imageRequest.getData(); //data:image/png;base64,
        int splitIndex = imageData.indexOf(",");
        String imageDataBase64 = imageData.substring(splitIndex + 1);
        byte[] imageDataBytes = Base64.decodeBase64(imageDataBase64);

        if (imageDataBytes != null && imageDataBytes.length > 0) {
            orgId = AuthUtils.fixOrgId(orgId);
            organizationDomain.saveOrgLogo(orgId, imageName, imageDataBytes);
        }
    }


    @RequestMapping(value = "{id}/brand_logo", method = RequestMethod.PUT)
    // @PreAuthorize("hasPermission(#orgId, 'orgId', 'organization:write')")
    public void saveBrandLogo(@PathVariable(value = "id") String orgId,
                              @RequestBody @Valid ImageRequest imageRequest) {

        String imageData = imageRequest.getData(); //data:image/png;base64,
        int splitIndex = imageData.indexOf(",");
        String imageDataBase64 = imageData.substring(splitIndex + 1);
        byte[] imageDataBytes = Base64.decodeBase64(imageDataBase64);

        if (imageDataBytes != null && imageDataBytes.length > 0) {
            orgId = AuthUtils.fixOrgId(orgId);
            organizationDomain.saveOrgLogo(orgId, imageDataBytes);
        }
    }


    @RequestMapping(value = "{id}/webchatKeys", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization_config:write')")
    public void saveWebChatKeys(@PathVariable("id") String orgId, @RequestParam("file") MultipartFile keyFile, @RequestParam("type") String fileType) {
        if (keyFile == null)
            throw new NotFoundException("no file uploaded!");
        String fileName = keyFile.getOriginalFilename();
        switch (fileType) {
            case "key":
                fileName = "apiclient_key.pem";
                break;
            case "cert":
                fileName = "apiclient_cert.pem";
                break;
            default:
                break;
        }
        organizationDomain.saveWebChatKey(orgId, keyFile, fileName);
    }

}
