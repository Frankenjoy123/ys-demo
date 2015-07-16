package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.GroupPermissionPolicyObject;
import com.yunsoo.data.service.entity.GroupPermissionPolicyEntity;
import com.yunsoo.data.service.repository.GroupPermissionPolicyRepository;
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
@RequestMapping("/grouppermissionpolicy")
public class GroupPermissionPolicyController {

    @Autowired
    private GroupPermissionPolicyRepository groupPermissionPolicyRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<GroupPermissionPolicyObject> getPermissionsByAccountId(@RequestParam(value = "group_id") String groupId) {
        return groupPermissionPolicyRepository.findByGroupId(groupId).stream()
                .map(this::toGroupPermissionPolicyObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public GroupPermissionPolicyObject create(@RequestBody GroupPermissionPolicyObject groupPermissionPolicyObject) {
        GroupPermissionPolicyEntity entity = toGroupPermissionPolicyEntity(groupPermissionPolicyObject);
        entity.setId(null);
        if (entity.getCreatedDatetime() == null) {
            entity.setCreatedDatetime(DateTime.now());
        }
        return toGroupPermissionPolicyObject(groupPermissionPolicyRepository.save(entity));
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByGroupId(@RequestParam(value = "group_id") String groupId,
                                @RequestParam(value = "org_id", required = false) String orgId,
                                @RequestParam(value = "policy_code", required = false) String policyCode) {
        if (orgId == null) {
            if (policyCode == null) {
                groupPermissionPolicyRepository.deleteByGroupId(groupId);
            } else {
                groupPermissionPolicyRepository.deleteByGroupIdAndPolicyCode(groupId, policyCode);
            }
        } else {
            if (policyCode == null) {
                groupPermissionPolicyRepository.deleteByGroupIdAndOrgId(groupId, orgId);
            } else {
                groupPermissionPolicyRepository.deleteByGroupIdAndOrgIdAndPolicyCode(groupId, orgId, policyCode);
            }
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        groupPermissionPolicyRepository.delete(id);
    }

    GroupPermissionPolicyObject toGroupPermissionPolicyObject(GroupPermissionPolicyEntity entity) {
        if (entity == null) {
            return null;
        }
        GroupPermissionPolicyObject object = new GroupPermissionPolicyObject();
        object.setId(entity.getId());
        object.setGroupId(entity.getGroupId());
        object.setOrgId(entity.getOrgId());
        object.setPolicyCode(entity.getPolicyCode());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDatetime(entity.getCreatedDatetime());
        return object;
    }

    GroupPermissionPolicyEntity toGroupPermissionPolicyEntity(GroupPermissionPolicyObject object) {
        if (object == null) {
            return null;
        }
        GroupPermissionPolicyEntity entity = new GroupPermissionPolicyEntity();
        entity.setId(object.getId());
        entity.setGroupId(object.getGroupId());
        entity.setOrgId(object.getOrgId());
        entity.setPolicyCode(object.getPolicyCode());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDatetime(object.getCreatedDatetime());
        return entity;
    }
}
