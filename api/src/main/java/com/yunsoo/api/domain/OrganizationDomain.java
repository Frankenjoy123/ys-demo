package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/12
 * Descriptions:
 */
@Component
public class OrganizationDomain {

    @Autowired
    private RestClient dataAPIClient;

    public OrganizationObject getOrganizationById(String id) {
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
            return dataAPIClient.getFileStreamObject("file/s3?path=organization/{orgId}/logo/{imageName}", orgId, imageName);
        } catch (NotFoundException ex) {
            return null;
        }
    }
}
