package com.yunsoo.api.domain;

import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.LocationObject;
import com.yunsoo.common.data.object.OrgAgencyObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by  : Haitao
 * Created on  : 2015/9/2
 * Descriptions:
 */
@Component
public class OrgAgencyDomain {

    @Autowired
    private RestClient dataApiClient;


    public OrgAgencyObject getOrgAgencyById(String id) {
        try {
            return dataApiClient.get("organizationagency/{id}", OrgAgencyObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public Page<OrgAgencyObject> getOrgAgencyByOrgId(String orgId, String searchText, org.joda.time.LocalDate start, org.joda.time.LocalDate end, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId).append("search_text", searchText)
                .append("start_datetime", start).append("end_datetime", end)
                .append(pageable)
                .build();

        return dataApiClient.getPaged("organizationagency" + query, new ParameterizedTypeReference<List<OrgAgencyObject>>() {
        });
    }

    public List<LocationObject> getLocationsByFilter(String parentId) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("parent_id", parentId)
                .build();
        return dataApiClient.get("organizationagency/location" + query, new ParameterizedTypeReference<List<LocationObject>>() {
        });
    }


    public OrgAgencyObject createOrgAgency(OrgAgencyObject orgAgencyObject) {
        orgAgencyObject.setId(null);
        return dataApiClient.post("organizationagency", orgAgencyObject, OrgAgencyObject.class);
    }

    public void updateOrgAgency(OrgAgencyObject orgAgencyObject) {
        dataApiClient.put("organizationagency/{id}", orgAgencyObject, orgAgencyObject.getId());
    }

    public void updateStatus(String agencyId, String statusCode) {
        if (!StringUtils.isEmpty(agencyId)) {
            OrgAgencyObject orgAgencyObject = getOrgAgencyById(agencyId);
            if (orgAgencyObject != null) {
                orgAgencyObject.setStatusCode(statusCode);
                orgAgencyObject.setModifiedAccountId(AuthUtils.getCurrentAccount().getId());
                orgAgencyObject.setModifiedDateTime(DateTime.now());
                dataApiClient.put("organizationagency/{id}", orgAgencyObject, agencyId);
            }
        }
    }

    public void deleteOrgAgency(String id) {
        dataApiClient.delete("organizationagency/{id}", id);
    }


}
