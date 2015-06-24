package com.yunsoo.api.controller;

import com.yunsoo.api.domain.GroupDomain;
import com.yunsoo.api.dto.Group;
import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.GroupObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private GroupDomain groupDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;


    @PostAuthorize("hasPermission(returnObject, 'group:read')")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Group getById(@PathVariable("id") String id) {
        GroupObject groupObject = groupDomain.getById(id);
        if (groupObject == null) {
            throw new NotFoundException("group not found");
        }
        return toGroup(groupObject);
    }

    @PreAuthorize("hasPermission(#orgId, 'filterByOrg', 'group:read')")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Group> getByOrgId(@RequestParam(value = "org_id", required = false) String orgId) {
        if (orgId == null) {
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return groupDomain.getByOrgId(orgId).stream().map(this::toGroup).collect(Collectors.toList());
    }


    @PreAuthorize("hasPermission(#group, 'group:create')")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Group create(@RequestBody @Valid Group group) {
        GroupObject groupObject = toGroupObject(group);
        TAccount currentAccount = tokenAuthenticationService.getAuthentication().getDetails();

        groupObject.setId(null);
        if (groupObject.getOrgId() == null) {
            groupObject.setOrgId(currentAccount.getOrgId());
        }
        groupObject.setCreatedAccountId(currentAccount.getId());
        groupObject.setCreatedDateTime(DateTime.now());
        groupObject.setModifiedAccountId(null);
        groupObject.setModifiedDatetime(null);
        groupObject = groupDomain.create(groupObject);
        return toGroup(groupObject);
    }

    @PreAuthorize("hasPermission(#group, 'group:modify')")
    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable("id") String id, @RequestBody Group group) {
        //todo

    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        //todo

    }

    private Group toGroup(GroupObject object) {
        if (object == null) {
            return null;
        }
        Group group = new Group();
        group.setId(object.getId());
        group.setOrgId(object.getOrgId());
        group.setName(object.getName());
        group.setDescription(object.getDescription());
        group.setCreatedAccountId(object.getCreatedAccountId());
        group.setCreatedDateTime(object.getCreatedDateTime());
        group.setModifiedAccountId(object.getModifiedAccountId());
        group.setModifiedDatetime(object.getModifiedDatetime());
        return group;
    }

    private GroupObject toGroupObject(Group group) {
        if (group == null) {
            return null;
        }
        GroupObject object = new GroupObject();
        object.setId(group.getId());
        object.setOrgId(group.getOrgId());
        object.setName(group.getName());
        object.setDescription(group.getDescription());
        object.setCreatedAccountId(group.getCreatedAccountId());
        object.setCreatedDateTime(group.getCreatedDateTime());
        object.setModifiedAccountId(group.getModifiedAccountId());
        object.setModifiedDatetime(group.getModifiedDatetime());
        return object;
    }

}
