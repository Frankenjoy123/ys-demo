package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.LocationObject;
import com.yunsoo.common.data.object.OrgAgencyObject;
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
 * Created by  : Haitao
 * Created on  : 2015/9/2
 * Descriptions:
 */
@Component
public class OrgAgencyDomain {

    @Autowired
    private RestClient dataAPIClient;


    public OrgAgencyObject getOrgAgencyById(String id) {
        try {
            return dataAPIClient.get("organizationagency/{id}", OrgAgencyObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public Page<OrgAgencyObject> getOrgAgencyByOrgId(String orgId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("organizationagency" + query, new ParameterizedTypeReference<List<OrgAgencyObject>>() {
        });
    }

    public List<LocationObject> getLocationsByFilter(String parentId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("parent_id", parentId)
                .build();
        return dataAPIClient.get("organizationagency/location" + query, new ParameterizedTypeReference<List<LocationObject>>() {
        });
    }


    public OrgAgencyObject createOrgAgency(OrgAgencyObject orgAgencyObject) {
        orgAgencyObject.setId(null);
        return dataAPIClient.post("organizationagency", orgAgencyObject, OrgAgencyObject.class);
    }

    public void updateOrgAgency(OrgAgencyObject orgAgencyObject) {
        dataAPIClient.put("organizationagency/{id}", orgAgencyObject, orgAgencyObject.getId());
    }

    public void deleteOrgAgency(String id) {
        dataAPIClient.delete("organizationagency/{id}", id);
    }


}
