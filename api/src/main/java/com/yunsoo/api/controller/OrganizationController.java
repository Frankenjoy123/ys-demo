package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.auth.dto.Account;
import com.yunsoo.api.auth.dto.AccountCreationRequest;
import com.yunsoo.api.auth.service.AuthAccountService;
import com.yunsoo.api.auth.service.AuthOrganizationService;
import com.yunsoo.api.auth.service.AuthPermissionService;
import com.yunsoo.api.domain.BrandDomain;
import com.yunsoo.api.domain.OrganizationDomain;
import com.yunsoo.api.dto.Attachment;
import com.yunsoo.api.dto.Brand;
import com.yunsoo.api.dto.ImageRequest;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.api.util.PageUtils;
import com.yunsoo.common.data.object.AttachmentObject;
import com.yunsoo.common.data.object.BrandObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
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
    private BrandDomain brandDomain;

    @Autowired
    private AuthAccountService authAccountService;

    @Autowired
    private AuthOrganizationService authOrganizationService;

    @Autowired
    private AuthPermissionService authPermissionService;

    @RequestMapping(value = "/brand", method = RequestMethod.POST)
    @PreAuthorize("hasPermission('current', 'org', 'organization:create')")
    public Brand createBrand(@RequestBody Brand brand) {
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        BrandObject object = brand.toBrand(brand);
        object.setCreatedAccountId(currentAccountId);
        Brand returnObj = new Brand(organizationDomain.createBrand(object));

        authPermissionService.addOrgIdToDefaultRegion(returnObj.getId());

        AccountCreationRequest accountCreationRequest = new AccountCreationRequest();
        accountCreationRequest.setEmail(returnObj.getEmail());
        accountCreationRequest.setIdentifier("admin");
        accountCreationRequest.setFirstName(returnObj.getContactName());
        accountCreationRequest.setLastName("");
        accountCreationRequest.setPassword("admin");
        accountCreationRequest.setPhone(returnObj.getContactMobile());
        accountCreationRequest.setOrgId(returnObj.getId());
        accountCreationRequest.setCreatedAccountId(Constants.Ids.SYSTEM_ACCOUNT_ID);
        Account account = authAccountService.create(accountCreationRequest);
        authPermissionService.allocateAdminPermissionOnCurrentOrgToAccount(account.getId());

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
            returnObject.setCarrier(authOrganizationService.getById(returnObject.getCarrierId()));

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
