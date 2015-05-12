package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
