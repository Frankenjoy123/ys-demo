package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.GroupPermissionPolicyObject;
import com.yunsoo.data.service.entity.GroupPermissionPolicyEntity;
import com.yunsoo.data.service.repository.GroupPermissionPolicyRepository;
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
@RequestMapping("/grouppermissionpolicy")
public class GroupPermissionPolicyController {

    @Autowired
    private GroupPermissionPolicyRepository groupPermissionPolicyRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<GroupPermissionPolicyObject> getPermissionsByAccountId(@RequestParam(value = "group_id") String groupId) {
        return groupPermissionPolicyRepository.findByGroupId(groupId).stream()
                .map(this::toGroupPermissionPolicyObject).collect(Collectors.toList());
    }

    GroupPermissionPolicyObject toGroupPermissionPolicyObject(GroupPermissionPolicyEntity entity) {
        if (entity == null) {
            return null;
        }
        GroupPermissionPolicyObject object = new GroupPermissionPolicyObject();
        object.setGroupId(entity.getGroupId());
        object.setOrgId(entity.getOrgId());
        object.setPolicyCode(entity.getPolicyCode());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDatetime(entity.getCreatedDatetime());
        return object;
    }
}
