package com.yunsoo.api.rabbit.auth.service;

import com.yunsoo.api.rabbit.auth.dto.Organization;
import com.yunsoo.api.rabbit.client.AuthApiClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-07-27
 * Descriptions:
 */
@Service
public class AuthOrganizationService {

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private AuthApiClient authApiClient;

    public Organization getById(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            return null;
        }
        try {
            return authApiClient.get("organization/{id}", Organization.class, orgId);
        } catch (NotFoundException ex) {
            log.warn("organization not found by id: " + orgId);
            return null;
        }
    }

    public List<Organization> getByIdsIn(List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("ids_in", ids)
                .build();
        return authApiClient.get("organization" + query, new ParameterizedTypeReference<List<Organization>>() {
        });
    }

}
