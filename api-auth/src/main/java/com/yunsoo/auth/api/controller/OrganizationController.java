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

//    @RequestMapping(value = "/brand", method = RequestMethod.POST)
//    @PreAuthorize("hasPermission('current', 'org', 'organization:create')")
//    public Brand createBrand(@RequestBody Brand brand) {
//        String currentAccountId = AuthUtils.getCurrentAccount().getId();
//        BrandObject object = brand.toBrand(brand);
//        object.setCreatedAccountId(currentAccountId);
//        Brand returnObj = new Brand(organizationService.createBrand(object));
//
//        permissionDomain.putOrgRestrictionToDefaultPermissionRegion(brand.getCarrierId(), returnObj.getId());
//
//        AccountObject accountObject = new AccountObject();
//        accountObject.setEmail(returnObj.getEmail());
//        accountObject.setIdentifier("admin");
//        accountObject.setFirstName(returnObj.getContactName());
//        accountObject.setLastName("");
//        accountObject.setPassword("admin");
//        accountObject.setPhone(returnObj.getContactMobile());
//        accountObject.setOrgId(returnObj.getId());
//        accountObject.setCreatedAccountId(Constants.Ids.SYSTEM_ACCOUNT_ID);
//        AccountObject createdAccount = accountDomain.createAccount(accountObject, true);
//        permissionAllocationDomain.allocateAdminPermissionOnCurrentOrgToAccount(createdAccount.getId());
//
//        return returnObj;
//    }

//    @RequestMapping(value = "/brand/{id}", method = RequestMethod.PATCH)
//    @PreAuthorize("hasPermission(#orgId, 'org', 'organization:write')")
//    public void patchBrand(@PathVariable("id") String id, @RequestBody Brand brand) {
//        organizationService.patchBrand(id, brand.toBrand(brand));
//    }
//
//    @RequestMapping(value = "/brand/{id}", method = RequestMethod.GET)
//    public Brand getBrandById(@PathVariable(value = "id") String id, @RequestParam(value = "carrier_info", required = false) boolean includeCarrier) {
//        BrandObject object = organizationService.getBrandById(id);
//        if (object == null) {
//            throw new NotFoundException("Brand not found by [id: " + id + "]");
//        }
//
//        Brand returnObject = new Brand(object);
//
//        if (StringUtils.hasText(object.getAttachment())) {
//            List<AttachmentObject> attachmentObjectList = brandDomain.getAttachmentList(object.getAttachment());
//            returnObject.setAttachmentList(attachmentObjectList.stream().map(Attachment::new).collect(Collectors.toList()));
//        }
//
//        if (includeCarrier)
//            returnObject.setCarrier(new Organization(organizationService.getOrganizationById(returnObject.getCarrierId())));
//
//        return returnObject;
//    }


//    @RequestMapping(value = "/{id}/brand/count", method = RequestMethod.GET)
//    public int countBrand(@PathVariable(value = "id") String id) {
//        return organizationService.countBrand(id, null);
//    }
//
//    @RequestMapping(value = "/{id}/brand", method = RequestMethod.GET)
//    @PostAuthorize("hasPermission(returnObject, 'organization:read')")
//    public List<Brand> filterOrgBrand(@PathVariable(value = "id") String id,
//                                      @RequestParam(value = "status", required = false) String status,
//                                      @RequestParam(value = "name", required = false) String name,
//                                      @RequestParam(value = "search_text", required = false) String searchText,
//                                      @RequestParam(value = "category_id", required = false) String categoryId,
//                                      @RequestParam(value = "start_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime startTime,
//                                      @RequestParam(value = "end_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime endTime,
//                                      Pageable pageable,
//                                      HttpServletResponse response) {
//        if (!StringUtils.hasText(searchText))
//            searchText = null;
//        Page<BrandObject> brandPage = organizationService.getOrgBrandList(id, name, status, searchText, startTime, endTime, categoryId, pageable);
//        if (pageable != null) {
//            response.setHeader("Content-Range", brandPage.toContentRange());
//        }
//
//        return brandPage.map(Brand::new).getContent();
//    }

//    @RequestMapping(value = "/{id}/brand/name", method = RequestMethod.GET)
//    @PostAuthorize("hasPermission(returnObject, 'organization:read')")
//    public List<Brand> filterOrgBrandByName(@PathVariable(value = "id") String id,
//                                            @RequestParam(value = "name", required = false) String name,
//                                            Pageable pageable,
//                                            HttpServletResponse response) {
//        if (!StringUtils.hasText(name))
//            name = null;
//        Page<BrandObject> brandPage = organizationService.getOrgBrandListByName(id, name, pageable);
//        if (pageable != null) {
//            response.setHeader("Content-Range", brandPage.toContentRange());
//        }
//
//        return brandPage.map(Brand::new).getContent();
//    }


//    @RequestMapping(value = "{id}/logo/{imageName}", method = RequestMethod.GET)
//    public ResponseEntity<?> getLogo(
//            @PathVariable(value = "id") String id,
//            @PathVariable(value = "imageName") String imageName) {
//        ResourceInputStream resourceInputStream = organizationService.getLogoImage(id, imageName);
//        if (resourceInputStream == null) {
//            throw new NotFoundException("logo not found");
//        }
//        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
//        builder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
//        if (resourceInputStream.getContentLength() > 0) {
//            builder.contentLength(resourceInputStream.getContentLength());
//        }
//        return builder.body(new InputStreamResource(resourceInputStream));
//    }


//    @RequestMapping(value = "{id}/logo", method = RequestMethod.PUT)
//    @PreAuthorize("hasPermission(#orgId, 'org', 'organization:write')")
//    public void saveOrgLogo(@PathVariable(value = "id") String orgId,
//                            @RequestBody byte[] imageDataBytes) {
//        if (imageDataBytes != null && imageDataBytes.length > 0) {
//            orgId = AuthUtils.fixOrgId(orgId);
//            organizationService.saveOrgLogo(orgId, imageDataBytes);
//        }
//    }

//    @RequestMapping(value = "{id}/logo/{imageName}", method = RequestMethod.PUT)
//    @PreAuthorize("hasPermission(#orgId, 'org', 'organization_config:write')")
//    public void saveCarrierLogo(@PathVariable(value = "id") String orgId, @PathVariable(value = "imageName") String imageName,
//                                @RequestBody @Valid ImageRequest imageRequest) {
//
//        String imageData = imageRequest.getData(); //data:image/png;base64,
//        int splitIndex = imageData.indexOf(",");
//        String imageDataBase64 = imageData.substring(splitIndex + 1);
//        byte[] imageDataBytes = Base64.decodeBase64(imageDataBase64);
//
//        if (imageDataBytes != null && imageDataBytes.length > 0) {
//            orgId = AuthUtils.fixOrgId(orgId);
//            organizationService.saveOrgLogo(orgId, imageName, imageDataBytes);
//        }
//    }


//    @RequestMapping(value = "{id}/brand_logo", method = RequestMethod.PUT)
//    // @PreAuthorize("hasPermission(#orgId, 'orgId', 'organization:write')")
//    public void saveBrandLogo(@PathVariable(value = "id") String orgId,
//                              @RequestBody @Valid ImageRequest imageRequest) {
//
//        String imageData = imageRequest.getData(); //data:image/png;base64,
//        int splitIndex = imageData.indexOf(",");
//        String imageDataBase64 = imageData.substring(splitIndex + 1);
//        byte[] imageDataBytes = Base64.decodeBase64(imageDataBase64);
//
//        if (imageDataBytes != null && imageDataBytes.length > 0) {
//            orgId = AuthUtils.fixOrgId(orgId);
//            organizationService.saveOrgLogo(orgId, imageDataBytes);
//        }
//    }


//    @RequestMapping(value = "{id}/webchatKeys", method = RequestMethod.POST)
//    @PreAuthorize("hasPermission(#orgId, 'org', 'organization_config:write')")
//    public void saveWebChatKeys(@PathVariable("id") String orgId, @RequestParam("file") MultipartFile keyFile, @RequestParam("type") String fileType) {
//        if (keyFile == null)
//            throw new NotFoundException("no file uploaded!");
//        String fileName = keyFile.getOriginalFilename();
//        switch (fileType) {
//            case "key":
//                fileName = "apiclient_key.pem";
//                break;
//            case "cert":
//                fileName = "apiclient_cert.pem";
//                break;
//            default:
//                break;
//        }
//        organizationService.saveWebChatKey(orgId, keyFile, fileName);
//    }
}