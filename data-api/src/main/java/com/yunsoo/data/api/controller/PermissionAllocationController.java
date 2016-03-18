package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.PermissionAllocationObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.PermissionAllocationEntity;
import com.yunsoo.data.service.repository.PermissionAllocationRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-03-17
 * Descriptions:
 */
@RestController
@RequestMapping("/permissionAllocation")
public class PermissionAllocationController {

    @Autowired
    private PermissionAllocationRepository permissionAllocationRepository;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public PermissionAllocationObject getPermissionAllocation(@PathVariable("id") String id) {
        PermissionAllocationEntity entity = permissionAllocationRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("permissionAllocation not found");
        }
        return toPermissionAllocationObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<PermissionAllocationObject> getPermissionAllocations(@RequestParam(value = "principal", required = true) String principal) {
        List<PermissionAllocationEntity> entities = permissionAllocationRepository.findByPrincipal(principal);
        return entities.stream().map(this::toPermissionAllocationObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public PermissionAllocationObject createPermissionAllocation(@RequestBody PermissionAllocationObject object) {
        PermissionAllocationEntity entity = toPermissionAllocationEntity(object);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        PermissionAllocationEntity newEntity = permissionAllocationRepository.save(entity);
        return toPermissionAllocationObject(newEntity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        PermissionAllocationEntity entity = permissionAllocationRepository.findOne(id);
        if (entity != null) {
            permissionAllocationRepository.delete(id);
        }
    }

    private PermissionAllocationObject toPermissionAllocationObject(PermissionAllocationEntity entity) {
        if (entity == null) {
            return null;
        }
        PermissionAllocationObject object = new PermissionAllocationObject();
        object.setId(entity.getId());
        object.setPrincipal(entity.getPrincipal());
        object.setRestriction(entity.getRestriction());
        object.setPermission(entity.getPermission());
        object.setEffect(PermissionAllocationObject.Effect.valueOf(entity.getEffect()));
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        return object;
    }

    private PermissionAllocationEntity toPermissionAllocationEntity(PermissionAllocationObject object) {
        if (object == null) {
            return null;
        }
        PermissionAllocationEntity entity = new PermissionAllocationEntity();
        entity.setId(object.getId());
        entity.setPrincipal(object.getPrincipal());
        entity.setRestriction(object.getRestriction());
        entity.setPermission(object.getPermission());
        entity.setEffect(object.getEffect().name());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }
}
