package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ApplicationObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.ApplicationEntity;
import com.yunsoo.data.service.repository.ApplicationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/6/15.
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
            throw new NotFoundException("Application not found by [id: " + id + "]");
        }
        return FromApplicationEntity(entity);
    }

    @RequestMapping(value = "type/{typeid}", method = RequestMethod.GET)
    public List<ApplicationObject> getByTypeId(@PathVariable String typeid) {
        List<String> activeStatus = new ArrayList<String>();
        activeStatus.add(LookupCodes.ApplicationStatus.ACTIVE);

        List<ApplicationObject> applicationObjectList = this.FromApplicationEntities(applicationRepository.findByTypeCodeAndStatusCodeIn(typeid, activeStatus, new PageRequest(0, 100)));
        return applicationObjectList;
    }

    private ApplicationObject FromApplicationEntity(ApplicationEntity entity) {
        if (entity == null) {
            return null;
        }
        ApplicationObject applicationObject = new ApplicationObject();
        BeanUtils.copyProperties(entity, applicationObject);
        return applicationObject;
    }

    private ApplicationEntity ToApplicationEntity(ApplicationObject applicationObject) {
        if (applicationObject == null) {
            return null;
        }
        ApplicationEntity entity = new ApplicationEntity();
        BeanUtils.copyProperties(applicationObject, entity);
        return entity;
    }

    private List<ApplicationObject> FromApplicationEntities(Iterable<ApplicationEntity> entities) {
        if (entities == null) {
            return null;
        }
        List<ApplicationObject> applicationObjectList = new ArrayList<>();
        for (ApplicationEntity entity : entities) {
            applicationObjectList.add(this.FromApplicationEntity(entity));
        }
        return applicationObjectList;
    }

    private List<ApplicationEntity> ToApplicationEntities(Iterable<ApplicationObject> applicationObjects) {
        if (applicationObjects == null) {
            return null;
        }
        List<ApplicationEntity> applicationEntityList = new ArrayList<>();
        for (ApplicationObject object : applicationObjects) {
            applicationEntityList.add(this.ToApplicationEntity(object));
        }
        return applicationEntityList;
    }

}