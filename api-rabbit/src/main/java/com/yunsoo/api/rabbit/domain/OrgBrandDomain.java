package com.yunsoo.api.rabbit.domain;

import com.yunsoo.common.data.object.OrgBrandObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by:   yan
 * Created on:   3/17/2016
 * Descriptions:
 */
@Component
public class OrgBrandDomain {

    @Autowired
    private RestClient dataApiClient;

    public OrgBrandObject getOrgBrandById(String orgId) {
        try {
            return dataApiClient.get("orgBrand/{id}", OrgBrandObject.class, orgId);
        } catch (NotFoundException ex) {
            return null;
        }
    }

}
