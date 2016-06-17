package com.yunsoo.api.domain;

import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ApplicationObject;
import com.yunsoo.common.web.client.Page;
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
 * Created by:   Lijian
 * Created on:   2015/9/7
 * Descriptions:
 */
@Component
public class ApplicationDomain {

    @Autowired
    private RestClient dataAPIClient;

    public ApplicationObject getApplicationById(String id) {
        try {
            return dataAPIClient.get("application/{id}", ApplicationObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public Page<ApplicationObject> getApplications(String typeCode, List<String> statusCodeIn, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("type_code", typeCode)
                .append("status_code_in", statusCodeIn)
                .append(pageable)
                .build();
        return dataAPIClient.getPaged("application" + query, new ParameterizedTypeReference<List<ApplicationObject>>() {
        });
    }

    public ApplicationObject createApplication(ApplicationObject applicationObject) {
        applicationObject.setId(null);
        applicationObject.setStatusCode(LookupCodes.ApplicationStatus.CREATED);
        applicationObject.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        applicationObject.setCreatedDateTime(DateTime.now());
        applicationObject.setModifiedAccountId(null);
        applicationObject.setModifiedDateTime(null);
        return dataAPIClient.post("application", applicationObject, ApplicationObject.class);
    }

    public void patchUpdateApplication(ApplicationObject applicationObject) {
        applicationObject.setCreatedAccountId(null);
        applicationObject.setCreatedDateTime(null);
        applicationObject.setModifiedAccountId(AuthUtils.getCurrentAccount().getId());
        applicationObject.setModifiedDateTime(DateTime.now());
        try {
            dataAPIClient.patch("application/{id}", applicationObject, applicationObject.getId());
        } catch (NotFoundException ignored) {
            throw new NotFoundException("application not found");
        }
    }
}
