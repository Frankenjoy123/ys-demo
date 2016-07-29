package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.api.security.authorization.AuthorizationService;
import com.yunsoo.auth.api.security.permission.PermissionEntryService;
import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.api.util.PageUtils;
import com.yunsoo.auth.dto.Group;
import com.yunsoo.auth.dto.PermissionEntry;
import com.yunsoo.auth.service.GroupService;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-07-07
 * Descriptions:
 */

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private PermissionEntryService permissionEntryService;


    //region group

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'group:read')")
    public Group getById(@PathVariable("id") String id) {
        return findGroupById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'group:read')")
    public List<Group> getByOrgId(@RequestParam(value = "org_id", required = false) String orgId,
                                  Pageable pageable,
                                  HttpServletResponse response) {
        orgId = AuthUtils.fixOrgId(orgId);

        Page<Group> page = groupService.getByOrgId(orgId, pageable);
        return PageUtils.response(response, page, pageable != null);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#group, 'group:create')")
    public Group create(@RequestBody @Valid Group group) {
        group.setOrgId(AuthUtils.fixOrgId(group.getOrgId()));
        return groupService.create(group);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdateGroup(@PathVariable("id") String id, @RequestBody Group group) {
        Group groupOld = findGroupById(id);
        AuthUtils.checkPermission(groupOld.getOrgId(), "group", "write");
        group.setId(id);
        groupService.patchUpdate(group);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        groupService.deleteGroupAndAllRelatedById(id);
    }

    //endregion


    //region permission

    @RequestMapping(value = "{group_id}/permission", method = RequestMethod.GET)
    public List<PermissionEntry> getAllPermissionByGroupId(@PathVariable("group_id") String groupId) {
        Group group = findGroupById(groupId);
        String orgId = group.getOrgId();

        AuthUtils.checkPermission(orgId, "permission_allocation", "read");

        return permissionEntryService.getExpendedPermissionEntriesByGroupId(groupId).stream()
                .map(p -> {
                    //fix orgRestriction
                    p.setRestriction(authorizationService.fixOrgRestriction(p.getRestriction(), orgId));
                    return new PermissionEntry(p);
                })
                .collect(Collectors.toList());
    }

    //endregion

    private Group findGroupById(String id) {
        Group group = groupService.getById(id);
        if (group == null) {
            throw new NotFoundException("group not found");
        }
        return group;
    }

}
