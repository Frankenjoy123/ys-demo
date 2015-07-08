package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.AccountPermissionObject;
import com.yunsoo.common.data.object.GroupPermissionObject;
import com.yunsoo.data.service.entity.GroupPermissionEntity;
import com.yunsoo.data.service.repository.GroupPermissionRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/24
 * Descriptions:
 */
@RestController
@RequestMapping("/grouppermission")
public class GroupPermissionController {

    @Autowired
    private GroupPermissionRepository groupPermissionRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<GroupPermissionObject> getPermissionsByAccountId(@RequestParam(value = "group_id") String groupId) {
        return groupPermissionRepository.findByGroupId(groupId).stream()
                .map(this::toGroupPermissionObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public GroupPermissionObject create(@RequestBody GroupPermissionObject groupPermissionObject) {
        GroupPermissionEntity entity = toGroupPermissionEntity(groupPermissionObject);
        if (entity.getCreatedDatetime() == null) {
            entity.setCreatedDatetime(DateTime.now());
        }
        entity.setId(null);
        return toGroupPermissionObject(groupPermissionRepository.save(entity));
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByGroupId(@RequestParam(value = "group_id") String groupId) {
        groupPermissionRepository.deleteByGroupId(groupId);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        groupPermissionRepository.delete(id);
    }

    GroupPermissionObject toGroupPermissionObject(GroupPermissionEntity entity) {
        if (entity == null) {
            return null;
        }
        GroupPermissionObject object = new GroupPermissionObject();
        object.setId(entity.getId());
        object.setGroupId(entity.getGroupId());
        object.setOrgId(entity.getOrgId());
        object.setResourceCode(entity.getResourceCode());
        object.setActionCode(entity.getActionCode());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDatetime(entity.getCreatedDatetime());
        return object;
    }

    GroupPermissionEntity toGroupPermissionEntity(GroupPermissionObject object) {
        if (object == null) {
            return null;
        }
        GroupPermissionEntity entity = new GroupPermissionEntity();
        entity.setId(object.getId());
        entity.setGroupId(object.getGroupId());
        entity.setOrgId(object.getOrgId());
        entity.setResourceCode(object.getResourceCode());
        entity.setActionCode(object.getActionCode());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDatetime(object.getCreatedDatetime());
        return entity;
    }



}
