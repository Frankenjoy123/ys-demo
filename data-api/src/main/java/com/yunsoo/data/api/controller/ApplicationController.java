package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ApplicationObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.ApplicationEntity;
import com.yunsoo.data.service.repository.ApplicationRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Zhe
 * Created on:   2015/6/15
 * Descriptions:
 */
@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ApplicationObject getById(@PathVariable String id) {
        ApplicationEntity entity = applicationRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("application not found by [id: " + id + "]");
        }
        return toApplicationObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ApplicationObject> getByTypeId(@RequestParam(value = "type_code", required = false) String typeCode,
                                               @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn,
                                               Pageable pageable,
                                               HttpServletResponse response) {
        Page<ApplicationEntity> entityPage;
        if (!StringUtils.isEmpty(typeCode)) {
            if (statusCodeIn != null) {
                entityPage = applicationRepository.findByTypeCodeAndStatusCodeIn(typeCode, statusCodeIn, pageable);
            } else {
                entityPage = applicationRepository.findByTypeCode(typeCode, pageable);
            }
        } else {
            if (statusCodeIn != null) {
                entityPage = applicationRepository.findByStatusCodeIn(statusCodeIn, pageable);
            } else {
                entityPage = applicationRepository.findAll(pageable);
            }
        }
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }
        return entityPage.getContent().stream().map(this::toApplicationObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ApplicationObject create(@RequestBody @Valid ApplicationObject applicationObject) {
        ApplicationEntity entity = toApplicationEntity(applicationObject);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        ApplicationEntity newEntity = applicationRepository.save(entity);
        return toApplicationObject(newEntity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable String id, @RequestBody ApplicationObject applicationObject) {
        ApplicationEntity entity = applicationRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("application not found by [id: " + id + "]");
        }
        if (applicationObject.getName() != null) {
            entity.setName(applicationObject.getName());
        }
        if (applicationObject.getVersion() != null) {
            entity.setVersion(applicationObject.getVersion());
        }
        if (applicationObject.getTypeCode() != null) {
            entity.setTypeCode(applicationObject.getTypeCode());
        }
        if (applicationObject.getStatusCode() != null) {
            entity.setStatusCode(applicationObject.getStatusCode());
        }
        if (applicationObject.getDescription() != null) {
            entity.setDescription(applicationObject.getDescription());
        }
        if (applicationObject.getModifiedAccountId() != null) {
            entity.setModifiedAccountId(applicationObject.getModifiedAccountId());
        }
        entity.setModifiedDateTime(applicationObject.getModifiedDateTime() != null ? applicationObject.getModifiedDateTime() : DateTime.now());
        applicationRepository.save(entity);
    }


    private ApplicationObject toApplicationObject(ApplicationEntity entity) {
        if (entity == null) {
            return null;
        }
        ApplicationObject object = new ApplicationObject();
        object.setId(entity.getId());
        object.setName(entity.getName());
        object.setVersion(entity.getVersion());
        object.setTypeCode(entity.getTypeCode());
        object.setStatusCode(entity.getStatusCode());
        object.setDescription(entity.getDescription());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        return object;
    }

    private ApplicationEntity toApplicationEntity(ApplicationObject object) {
        if (object == null) {
            return null;
        }
        ApplicationEntity entity = new ApplicationEntity();
        entity.setId(object.getId());
        entity.setName(object.getName());
        entity.setVersion(object.getVersion());
        entity.setTypeCode(object.getTypeCode());
        entity.setStatusCode(object.getStatusCode());
        entity.setDescription(object.getDescription());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        return entity;
    }

}