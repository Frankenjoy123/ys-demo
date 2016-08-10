package com.yunsoo.auth.service;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.api.util.PageUtils;
import com.yunsoo.auth.dao.entity.OrganizationEntity;
import com.yunsoo.auth.dao.repository.OrganizationRepository;
import com.yunsoo.auth.dto.Organization;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Organization create(Organization org) {
        if (!Constants.OrgType.ALL.contains(org.getTypeCode())) {
            throw new BadRequestException("type_code invalid");
        }
        if (organizationRepository.findByName(org.getName()).size() > 0) {
            throw new ConflictException("organization already exists with the same name: " + org.getName());
        }
        OrganizationEntity entity = new OrganizationEntity();
        if (!StringUtils.isEmpty(org.getId())) {
            entity.setId(org.getId()); //if id is not null or empty, create the org with the given id
        }
        entity.setName(org.getName());
        entity.setTypeCode(org.getTypeCode());
        entity.setStatusCode(Constants.OrgStatus.CREATED);
        entity.setDescription(org.getDescription());
        entity.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        entity.setCreatedDateTime(DateTime.now());
        entity = organizationRepository.save(entity);
        log.info("organization created. " + StringFormatter.formatMap("id", entity.getId(), "name", org.getName(), "typeCode", org.getTypeCode()));
        return toOrganization(entity);
    }

    @Transactional
    public void patchUpdate(Organization org) {
        if (StringUtils.isEmpty(org.getId())) {
            return;
        }
        OrganizationEntity entity = organizationRepository.findOne(org.getId());
        if (entity == null) {
            throw new NotFoundException("organization not found by id: " + org.getId());
        }
        if (StringUtils.hasText(org.getName()) && !org.getName().equals(entity.getName())) {
            if (organizationRepository.findByName(org.getName()).size() > 0) {
                throw new ConflictException("organization already exists with the same name: " + org.getName());
            }
            entity.setName(org.getName());
        }
        if (org.getDescription() != null) {
            entity.setDescription(org.getDescription());
        }
        organizationRepository.save(entity);
    }

    @Transactional
    public Organization save(Organization org) {
        if (StringUtils.isEmpty(org.getId()) || organizationRepository.findOne(org.getId()) == null) {
            return create(org);
        } else {
            patchUpdate(org);
            return org;
        }
    }

    @Transactional
    public void updateStatus(String orgId, String statusCode) {
        if (StringUtils.isEmpty(orgId) || !Constants.OrgStatus.ALL.contains(statusCode)) {
            return;
        }
        OrganizationEntity entity = organizationRepository.findOne(orgId);
        if (entity == null) {
            throw new NotFoundException("organization not found by id: " + orgId);
        }
        entity.setStatusCode(statusCode);
        organizationRepository.save(entity);
    }

    @Transactional
    public void delete(String orgId) {
        if (!StringUtils.isEmpty(orgId)) {
            OrganizationEntity entity = organizationRepository.findOne(orgId);
            if (entity != null) {
                if (Constants.OrgStatus.CREATED.equals(entity.getStatusCode())) {
                    organizationRepository.delete(entity);
                } else {
                    throw new UnprocessableEntityException("organization can not be deleted on status: " + entity.getStatusCode());
                }
            }
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
