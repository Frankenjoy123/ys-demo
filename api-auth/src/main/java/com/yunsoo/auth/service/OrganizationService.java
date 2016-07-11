package com.yunsoo.auth.service;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.dao.entity.OrganizationEntity;
import com.yunsoo.auth.dao.repository.OrganizationRepository;
import com.yunsoo.auth.dto.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-07-06
 * Descriptions:
 */
@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public Organization getById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        OrganizationEntity entity = organizationRepository.findOne(id);
        return toOrganization(entity);
    }

    public Organization getByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        List<OrganizationEntity> entities = organizationRepository.findByName(name);
        if (entities.size() == 0) {
            return null;
        }
        return toOrganization(entities.get(0));
    }


    public boolean validateOrganization(Organization organization) {
        return organization != null && Constants.OrgStatus.AVAILABLE.equals(organization.getStatusCode());
    }

    private Organization toOrganization(OrganizationEntity entity) {
        if (entity == null) {
            return null;
        }
        Organization organization = new Organization();
        organization.setId(entity.getId());
        organization.setName(entity.getName());
        organization.setTypeCode(entity.getTypeCode());
        organization.setStatusCode(entity.getStatusCode());
        organization.setDescription(entity.getDescription());
        organization.setCreatedAccountId(entity.getCreatedAccountId());
        organization.setCreatedDateTime(entity.getCreatedDateTime());
        return organization;
    }
}
