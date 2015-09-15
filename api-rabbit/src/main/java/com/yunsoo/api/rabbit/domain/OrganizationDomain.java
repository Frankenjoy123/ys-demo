package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/12
 * Descriptions:
 */
@Component
public class OrganizationDomain {

    private static final String DEFAULT_LOGO_IMAGE_NAME = "image-128x128";

    @Autowired
    private RestClient dataAPIClient;

    public OrganizationObject getById(String id) {
        try {
            return dataAPIClient.get("organization/{id}", OrganizationObject.class, id);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public ResourceInputStream getLogoImage(String orgId, String imageName) {
        if (StringUtils.isEmpty(imageName)) {
            imageName = DEFAULT_LOGO_IMAGE_NAME;
        }
        try {
            return dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/logo/{imageName}", orgId, imageName);
        } catch (NotFoundException ex) {
            return null;
        }
    }
}
