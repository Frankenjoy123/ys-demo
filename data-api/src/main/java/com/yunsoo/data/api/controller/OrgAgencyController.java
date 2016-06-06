package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.OrgAgencyObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.OrgAgencyEntity;
import com.yunsoo.data.service.repository.OrgAgencyRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Haitao
 * Created on:   2015/9/2
 * Descriptions:
 */
@RestController
@RequestMapping("/organizationagency")
public class OrgAgencyController {

    @Autowired
    private OrgAgencyRepository orgAgencyRepository;

    //get organization agency by id
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public OrgAgencyObject getById(@PathVariable(value = "id") String id) {
        OrgAgencyEntity entity = orgAgencyRepository.findOne(id);
        if (entity == null || LookupCodes.OrgAgencyStatus.DEACTIVATED.equals(entity.getStatusCode())) {
            throw new NotFoundException("organization agency not found by [id:" + id + "]");
        }
        return toOrgAgencyObject(entity);
    }

    //query
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<OrgAgencyObject> getByFilter(@RequestParam(value = "org_id") String orgId,
                                             Pageable pageable,
                                             HttpServletResponse response) {
        Page<OrgAgencyEntity> entityPage = orgAgencyRepository.findByOrgIdAndStatusCode(orgId, LookupCodes.OrgAgencyStatus.ACTIVATED, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }

        return entityPage.getContent().stream()
                .map(this::toOrgAgencyObject)
                .collect(Collectors.toList());
    }

    //create
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public OrgAgencyObject create(@RequestBody OrgAgencyObject orgAgencyObject) {
        OrgAgencyEntity entity = toOrgAgencyEntity(orgAgencyObject);
        entity.setId(null);
        entity.setStatusCode(LookupCodes.OrgAgencyStatus.ACTIVATED);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setModifiedAccountId(null);
        entity.setModifiedDateTime(null);
        OrgAgencyEntity newEntity = orgAgencyRepository.save(entity);
        return toOrgAgencyObject(newEntity);
    }

    //update
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void update(@PathVariable(value = "id") String id,
                       @RequestBody OrgAgencyObject orgAgencyObject) {
        OrgAgencyEntity oldentity = orgAgencyRepository.findOne(id);
        if (oldentity != null && LookupCodes.OrgAgencyStatus.ACTIVATED.equals(oldentity.getStatusCode())) {
            OrgAgencyEntity entity = toOrgAgencyEntity(orgAgencyObject);
            orgAgencyRepository.save(entity);
        }
    }

    //delete
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {
        OrgAgencyEntity entity = orgAgencyRepository.findOne(id);
        if (entity != null && LookupCodes.OrgAgencyStatus.ACTIVATED.equals(entity.getStatusCode())) {
            entity.setStatusCode(LookupCodes.OrgAgencyStatus.DEACTIVATED);
            orgAgencyRepository.save(entity);
        }
    }


    private OrgAgencyObject toOrgAgencyObject(OrgAgencyEntity entity) {
        if (entity == null) {
            return null;
        }
        OrgAgencyObject object = new OrgAgencyObject();
        object.setId(entity.getId());
        object.setName(entity.getName());
        object.setOrgId(entity.getOrgId());
        object.setLocationIds(Arrays.asList(StringUtils.commaDelimitedListToStringArray(entity.getLocationIds())));
        object.setParentId(entity.getParentId());
        object.setAddress(entity.getAddress());
        object.setDescription(entity.getDescription());
        object.setStatusCode(entity.getStatusCode());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        object.setAgencyResponsible(entity.getAgencyResponsible());
        object.setAgencyCode(entity.getAgencyCode());
        object.setAgencyPhone(entity.getAgencyPhone());
        return object;
    }

    private OrgAgencyEntity toOrgAgencyEntity(OrgAgencyObject object) {
        if (object == null) {
            return null;
        }
        OrgAgencyEntity entity = new OrgAgencyEntity();
        entity.setId(object.getId());
        entity.setName(object.getName());
        entity.setOrgId(object.getOrgId());
        entity.setLocationIds(StringUtils.collectionToCommaDelimitedString(object.getLocationIds()));
        entity.setParentId(object.getParentId());
        entity.setAddress(object.getAddress());
        entity.setDescription(object.getDescription());
        entity.setStatusCode(object.getStatusCode());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        entity.setAgencyResponsible(object.getAgencyResponsible());
        entity.setAgencyCode(object.getAgencyCode());
        entity.setAgencyPhone(object.getAgencyPhone());
        return entity;
    }
}
