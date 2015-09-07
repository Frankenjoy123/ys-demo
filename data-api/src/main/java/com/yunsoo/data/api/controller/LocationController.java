package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.LocationObject;
import com.yunsoo.data.service.entity.LocationEntity;
import com.yunsoo.data.service.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Haitao
 * Created on:   2015/9/6
 * Descriptions:
 */
@RestController
@RequestMapping("/organizationagency/location")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<LocationObject> getAllLoations(@RequestParam(value = "parent_id", required = false) String parentId) {
        List<LocationEntity> entities;
        if (parentId == null) {
            entities = locationRepository.findAll();
        } else {
            entities = locationRepository.findByParentId(parentId);
        }
        return entities.stream().map(this::toLocationObject).collect(Collectors.toList());
    }

    private LocationObject toLocationObject(LocationEntity entity) {
        if (entity == null) {
            return null;
        }
        LocationObject object = new LocationObject();
        object.setId(entity.getId());
        object.setLatitude(entity.getLatitude());
        object.setLongitude(entity.getLongitude());
        object.setName(entity.getName());
        object.setTypeCode(entity.getTypeCode());
        object.setParentId(entity.getParentId());
        object.setDescription(entity.getDescription());
        return object;
    }

    private LocationEntity toLocationEntity(LocationObject object) {
        if (object == null) {
            return null;
        }
        LocationEntity entity = new LocationEntity();
        entity.setId(object.getId());
        entity.setLatitude(object.getLatitude());
        entity.setLongitude(object.getLongitude());
        entity.setName(object.getName());
        entity.setTypeCode(object.getTypeCode());
        entity.setParentId(object.getParentId());
        entity.setDescription(object.getDescription());
        return entity;
    }

}
