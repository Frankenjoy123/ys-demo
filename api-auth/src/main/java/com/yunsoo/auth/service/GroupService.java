package com.yunsoo.auth.service;

import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.api.util.PageUtils;
import com.yunsoo.auth.dao.entity.GroupEntity;
import com.yunsoo.auth.dao.repository.GroupRepository;
import com.yunsoo.auth.dto.Group;
import com.yunsoo.common.web.client.Page;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-07-07
 * Descriptions:
 */
@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AccountGroupService accountGroupService;

    @Autowired
    private PermissionAllocationService permissionAllocationService;


    public Group getById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return toGroup(groupRepository.findOne(id));
    }

    public List<Group> getByIds(List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return groupRepository.findAll(ids).stream().map(this::toGroup).collect(Collectors.toList());
    }

    public Page<Group> getByOrgId(String orgId, Pageable pageable) {
        if (StringUtils.isEmpty(orgId)) {
            return Page.empty();
        }
        return PageUtils.convert(groupRepository.findByOrgId(orgId, pageable)).map(this::toGroup);
    }

    public List<Group> getByOrgId(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            return new ArrayList<>();
        }
        return groupRepository.findByOrgId(orgId, null).getContent().stream().map(this::toGroup).collect(Collectors.toList());
    }

    public Group create(Group group) {
        Assert.notNull(group.getOrgId());
        Assert.notNull(group.getName());
        GroupEntity entity = new GroupEntity();
        entity.setOrgId(group.getOrgId());
        entity.setName(group.getName());
        entity.setDescription(group.getDescription());
        entity.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        entity.setCreatedDateTime(DateTime.now());
        return toGroup(groupRepository.save(entity));
    }

    @Transactional
    public void patchUpdate(Group group) {
        if (StringUtils.isEmpty(group.getId())) {
            return;
        }
        GroupEntity entity = groupRepository.findOne(group.getId());
        if (entity != null) {
            if (group.getName() != null) entity.setName(group.getName());
            if (group.getDescription() != null) entity.setDescription(group.getDescription());
            entity.setModifiedAccountId(AuthUtils.getCurrentAccount().getId());
            entity.setModifiedDateTime(DateTime.now());
            groupRepository.save(entity);
        }
    }

    @Transactional
    public void deleteGroupAndAllRelatedById(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return;
        }
        GroupEntity entity = groupRepository.findOne(groupId);
        if (entity != null) {
            //delete group related sources in order
            permissionAllocationService.deletePermissionAllocationsByGroupId(groupId);
            accountGroupService.deleteAccountGroupsByGroupId(groupId);
            groupRepository.delete(entity);
        }
    }


    private Group toGroup(GroupEntity entity) {
        if (entity == null) {
            return null;
        }
        Group group = new Group();
        group.setId(entity.getId());
        group.setOrgId(entity.getOrgId());
        group.setName(entity.getName());
        group.setDescription(entity.getDescription());
        group.setCreatedAccountId(entity.getCreatedAccountId());
        group.setCreatedDateTime(entity.getCreatedDateTime());
        group.setModifiedAccountId(entity.getModifiedAccountId());
        group.setModifiedDateTime(entity.getModifiedDateTime());
        return group;
    }

}
