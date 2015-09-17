package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ElastiCacheConfig;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/12
 * Descriptions:
 */
@Component
@ElastiCacheConfig
public class OrganizationDomain {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationDomain.class);

    @Autowired
    private RestClient dataAPIClient;

    private static final String ORG_LOGO_IMAGE_128X128 = "image-128x128";

    private static final String ORG_LOGO_IMAGE_200X200 = "image-200x200";


    @Cacheable(key="T(com.yunsoo.api.cache.CustomKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).ORGANIZATION.toString(), #id)")
    public OrganizationObject getOrganizationById(String id) {
        LOGGER.debug("cache missing on organization." + id );
        try {
            return dataAPIClient.get("organization/{id}", OrganizationObject.class, id);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public OrganizationObject getOrganizationByName(String name) {
        List<OrganizationObject> objects = dataAPIClient.get("organization?name={name}", new ParameterizedTypeReference<List<OrganizationObject>>() {
        }, name);
        return objects.size() == 0 ? null : objects.get(0);
    }

    public Page<OrganizationObject> getOrganizationList(Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append(pageable)
                .build();
        return dataAPIClient.getPaged("organization" + query, new ParameterizedTypeReference<List<OrganizationObject>>() {
        });
    }

    public OrganizationObject createOrganization(OrganizationObject object) {
        object.setId(null);
        object.setCreatedDateTime(DateTime.now());
        return dataAPIClient.post("organization", object, OrganizationObject.class);
    }

    public ResourceInputStream getLogoImage(String orgId, String imageName) {
        try {
            return dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/logo/{imageName}", orgId, imageName);
        } catch (NotFoundException ex) {
            return null;
        }

    }

    public void saveOrgLogo(String orgId, byte[] imageDataBytes) {
        String logoImage128x128 = ORG_LOGO_IMAGE_128X128;
        String logoImage200x200 = ORG_LOGO_IMAGE_200X200;
        try {
            //128x128
            ImageProcessor imageProcessor = new ImageProcessor().read(new ByteArrayInputStream(imageDataBytes));
            ByteArrayOutputStream logo128x128OutputStream = new ByteArrayOutputStream();
            imageProcessor.resize(128, 128).write(logo128x128OutputStream, "png");
            dataAPIClient.put("file/s3?path=organization/{orgId}/logo/{imageName}",
                    new ResourceInputStream(new ByteArrayInputStream(logo128x128OutputStream.toByteArray()), logo128x128OutputStream.size(), "image/png"),
                    orgId, logoImage128x128);
            //200x200
            ByteArrayOutputStream logo200x200OutputStream = new ByteArrayOutputStream();
            imageProcessor.resize(200, 200).write(logo200x200OutputStream, "png");
            dataAPIClient.put("file/s3?path=organization/{orgId}/logo/{imageName}",
                    new ResourceInputStream(new ByteArrayInputStream(logo200x200OutputStream.toByteArray()), logo200x200OutputStream.size(), "image/png"),
                    orgId, logoImage200x200);
        } catch (IOException e) {
            throw new InternalServerErrorException("logo upload failed [orgId: " + orgId + "]");
        }
    }

}
