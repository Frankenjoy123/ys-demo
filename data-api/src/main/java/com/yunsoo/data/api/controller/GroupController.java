package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.GroupObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.GroupEntity;
import com.yunsoo.data.service.repository.GroupRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/24
 * Descriptions:
 */
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public GroupObject getById(@PathVariable("id") String id) {
        GroupEntity entity = getGroupEntityById(id);
        return toGroupObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<GroupObject> getByOrgId(@RequestParam("org_id") String orgId) {
        List<GroupEntity> entities = groupRepository.findByOrgId(orgId);
        return entities.stream().map(this::toGroupObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public GroupObject create(@RequestBody @Valid GroupObject object) {
        GroupEntity entity = toGroupEntity(object);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setModifiedAccountId(null);
        entity.setModifiedDatetime(null);
        GroupEntity newEntity = groupRepository.save(entity);
        return toGroupObject(newEntity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable("id") String id, @RequestBody GroupObject object) {
        GroupEntity entity = getGroupEntityById(id);
        if (object.getName() != null) {
            entity.setName(object.getName());
        }
        if (object.getDescription() != null) {
            entity.setDescription(object.getDescription());
        }
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDatetime(object.getModifiedDatetime() == null ? DateTime.now() : object.getModifiedDatetime());
        groupRepository.save(entity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        GroupEntity entity = groupRepository.findOne(id);
        if (entity != null) {
            groupRepository.delete(id);
        }
    }

    private GroupEntity getGroupEntityById(String id) {
        GroupEntity entity = groupRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("group not found by [id: " + id + "]");
        }
        return entity;
    }

    private GroupObject toGroupObject(GroupEntity entity) {
        if (entity == null) {
            return null;
        }
        GroupObject object = new GroupObject();
        object.setId(entity.getId());
        object.setOrgId(entity.getOrgId());
        object.setName(entity.getName());
        object.setDescription(entity.getDescription());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setModifiedAccountId(entity.getModifiedAccountId());
        object.setModifiedDatetime(entity.getModifiedDatetime());
        return object;
    }

    private GroupEntity toGroupEntity(GroupObject object) {
        if (object == null) {
            return null;
        }
        GroupEntity entity = new GroupEntity();
        entity.setId(object.getId());
        entity.setOrgId(object.getOrgId());
        entity.setName(object.getName());
        entity.setDescription(object.getDescription());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDatetime(object.getModifiedDatetime());
        return entity;
    }
}
