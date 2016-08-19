package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.file.service.ImageService;
import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/12
 * Descriptions:
 */
@Component
@ObjectCacheConfig
public class OrganizationDomain {

    @Autowired
    private ImageService imageService;

    private static final String ORG_LOGO_IMAGE_128X128 = "image-128x128";
    private static final String ORG_LOGO_IMAGE_200X200 = "image-200x200";

    public void saveOrgLogo(String orgId, byte[] imageDataBytes) {
        saveOrgLogo(orgId, new ByteArrayInputStream(imageDataBytes));
    }

    public void saveOrgLogo(String orgId, InputStream logoStream) {
        String logoImage128x128 = ORG_LOGO_IMAGE_128X128;
        String logoImage200x200 = ORG_LOGO_IMAGE_200X200;
        try {
            //128x128
            ImageProcessor imageProcessor = new ImageProcessor().read(logoStream);
            ByteArrayOutputStream logo128x128OutputStream = new ByteArrayOutputStream();
            imageProcessor.resize(128, 128).write(logo128x128OutputStream, MediaType.IMAGE_PNG_VALUE);
            String filePath = String.format("organization/%s/logo/%s", orgId, logoImage128x128);
            imageService.saveImage(logo128x128OutputStream.toByteArray(), MediaType.IMAGE_PNG_VALUE, filePath);

            //200x200
            ByteArrayOutputStream logo200x200OutputStream = new ByteArrayOutputStream();
            imageProcessor.resize(200, 200).write(logo200x200OutputStream, MediaType.IMAGE_PNG_VALUE);
            filePath = String.format("organization/%s/logo/%s", orgId, logoImage200x200);
            imageService.saveImage(logo200x200OutputStream.toByteArray(), MediaType.IMAGE_PNG_VALUE, filePath);

        } catch (IOException e) {
            throw new InternalServerErrorException("logo upload failed [orgId: " + orgId + "]");
        }
    }


}
