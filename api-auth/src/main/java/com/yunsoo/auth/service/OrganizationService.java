package com.yunsoo.auth.service;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.api.util.PageUtils;
import com.yunsoo.auth.dao.entity.OrganizationEntity;
import com.yunsoo.auth.dao.repository.OrganizationRepository;
import com.yunsoo.auth.dto.Organization;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

    private Log log = LogFactory.getLog(this.getClass());

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

    public Page<Organization> getAll(Pageable pageable) {
        return PageUtils.convert(organizationRepository.findAll(pageable)).map(this::toOrganization);
    }

    public Page<Organization> getByIds(List<String> idsIn, Pageable pageable) {
        if (idsIn == null || idsIn.size() == 0) {
            return Page.empty();
        }
        return PageUtils.convert(organizationRepository.findByIdIn(idsIn, pageable)).map(this::toOrganization);
    }

    public Organization create(Organization org) {
        OrganizationEntity entity = new OrganizationEntity();
        entity.setName(org.getName());
        entity.setTypeCode(org.getTypeCode());
        entity.setStatusCode(Constants.OrgStatus.AVAILABLE);
        entity.setDescription(org.getDescription());
        entity.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        entity.setCreatedDateTime(DateTime.now());
        entity = organizationRepository.save(entity);
        log.info("organization created. " + StringFormatter.formatMap("name", org.getName(), "typeCode", org.getTypeCode()));
        return toOrganization(entity);
    }

    public void patchUpdate(Organization org) {
        if (StringUtils.isEmpty(org.getId())) {
            return;
        }
        OrganizationEntity entity = organizationRepository.findOne(org.getId());
        if (entity != null) {
            if (StringUtils.hasText(org.getName())) entity.setName(org.getName());
            if (org.getDescription() != null) entity.setDescription(org.getDescription());
            organizationRepository.save(entity);
        }
    }

    public void updateStatus(String orgId, String statusCode) {
        if (StringUtils.isEmpty(orgId) || !Constants.OrgStatus.ALL.contains(statusCode)) {
            return;
        }
        OrganizationEntity entity = organizationRepository.findOne(orgId);
        if (entity != null) {
            entity.setStatusCode(statusCode);
            organizationRepository.save(entity);
        }
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
