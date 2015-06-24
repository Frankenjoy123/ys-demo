package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.GroupPermissionObject;
import com.yunsoo.data.service.entity.GroupPermissionEntity;
import com.yunsoo.data.service.repository.GroupPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    GroupPermissionObject toGroupPermissionObject(GroupPermissionEntity entity) {
        if (entity == null) {
            return null;
        }
        GroupPermissionObject object = new GroupPermissionObject();
        object.setGroupId(entity.getGroupId());
        object.setOrgId(entity.getOrgId());
        object.setResourceCode(entity.getResourceCode());
        object.setActionCode(entity.getActionCode());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDatetime(entity.getCreatedDatetime());
        return object;
    }

}
