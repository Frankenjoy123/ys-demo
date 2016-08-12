package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RestClient dataApiClient;


    private static final String ORG_LOGO_IMAGE_128X128 = "image-128x128";

    private static final String ORG_LOGO_IMAGE_200X200 = "image-200x200";

    public ResourceInputStream getLogoImage(String orgId, String imageName) {
        try {
            return dataApiClient.getResourceInputStream("file/s3?path=organization/{orgId}/logo/{imageName}", orgId, imageName);
        } catch (NotFoundException ex) {
            return null;
        }

    }

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
            imageProcessor.resize(128, 128).write(logo128x128OutputStream, "image/png");
            dataApiClient.put("file/s3?path=organization/{orgId}/logo/{imageName}",
                    new ResourceInputStream(new ByteArrayInputStream(logo128x128OutputStream.toByteArray()), logo128x128OutputStream.size(), "image/png"),
                    orgId, logoImage128x128);
            //200x200
            ByteArrayOutputStream logo200x200OutputStream = new ByteArrayOutputStream();
            imageProcessor.resize(200, 200).write(logo200x200OutputStream, "image/png");
            dataApiClient.put("file/s3?path=organization/{orgId}/logo/{imageName}",
                    new ResourceInputStream(new ByteArrayInputStream(logo200x200OutputStream.toByteArray()), logo200x200OutputStream.size(), "image/png"),
                    orgId, logoImage200x200);
        } catch (IOException e) {
            throw new InternalServerErrorException("logo upload failed [orgId: " + orgId + "]");
        }
    }

    public void saveOrgLogo(String orgId, String imageName, byte[] imageDataBytes) {
        try {
            ImageProcessor imageProcessor = new ImageProcessor().read(new ByteArrayInputStream(imageDataBytes));
            ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
            imageProcessor.write(imageOutputStream, "image/png");
            dataApiClient.put("file/s3?path=organization/{orgId}/logo/{imageName}",
                    new ResourceInputStream(new ByteArrayInputStream(imageOutputStream.toByteArray()), imageOutputStream.size(), "image/png"),
                    orgId, imageName);
            log.info(String.format("image saved [imageName: %s]", imageName));


        } catch (IOException e) {
            throw new InternalServerErrorException("logo upload failed [orgId: " + orgId + "], imageName: " + imageName);
        }

    }


    public void saveWebChatKey(String orgId, MultipartFile file, String fileName) {
        String s3FileName = "organization/{orgId}/webchat/" + fileName;
        try {
            ResourceInputStream stream = new ResourceInputStream(file.getInputStream(), file.getSize(), file.getContentType());
            dataApiClient.put("file/s3?path=" + s3FileName, stream, orgId);
        } catch (IOException e) {
            throw new InternalServerErrorException("webchat key upload failed for organization: " + orgId);
        }
    }

}
