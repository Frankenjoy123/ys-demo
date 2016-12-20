package com.yunsoo.api.controller;

import com.yunsoo.api.domain.OrganizationDomain;
import com.yunsoo.api.file.dto.ImageRequest;
import com.yunsoo.api.file.service.FileService;
import com.yunsoo.api.file.service.ImageService;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.awt.*;
import java.io.IOException;

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
    private ImageService imageService;
    
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "{id}/logo/{imageName}", method = RequestMethod.GET)
    public ResponseEntity<?> getLogo(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "imageName") String imageName) {

        String path = String.format("organization/%s/logo/%s", id, imageName);
        return imageService.getImage(path);
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
                                @RequestBody @Valid ImageRequest imageRequest) throws IOException {

        String imageData = imageRequest.getData(); //data:image/png;base64,
        int splitIndex = imageData.indexOf(",");
        String imageDataBase64 = imageData.substring(splitIndex + 1);
        byte[] imageDataBytes = Base64.decodeBase64(imageDataBase64);

        if (imageDataBytes != null && imageDataBytes.length > 0) {
            orgId = AuthUtils.fixOrgId(orgId);
            imageService.saveImage(imageDataBytes, MediaType.IMAGE_PNG_VALUE, String.format("organization/%s/logo/%s", orgId, imageName));
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
    public void saveWebChatKeys(@PathVariable("id") String orgId, @RequestParam("file") MultipartFile keyFile, @RequestParam("type") String fileType) throws IOException {
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

        String filePath = String.format("organization/%s/webchat/%s", orgId, fileName);
        fileService.putFile(filePath, new ResourceInputStream(keyFile.getInputStream(), keyFile.getSize(), keyFile.getContentType()));
    }

}
