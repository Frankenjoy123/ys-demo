package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
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
        try {
            return dataAPIClient.get("organization?name={name}", OrganizationObject.class, name);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public Page<List<OrganizationObject>> getOrganizationList(Pageable pageable) {
        try {
            String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                    .append(pageable)
                    .build();
            return dataAPIClient.getPaged("organization/list?" + query, new ParameterizedTypeReference<List<OrganizationObject>>() {
            });
        } catch (NotFoundException ex) {
            return null;
        }
    }
}
