package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserOrganizationFollowingObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.UserOrganizationFollowingEntity;
import com.yunsoo.data.service.repository.UserOrganizationFollowingRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Zhe
 * Created on:   2015/4/15
 * Descriptions:
 */
@RestController
@RequestMapping("/UserOrganizationFollowing")
public class UserOrganizationFollowingController {

    @Autowired
    private UserOrganizationFollowingRepository userOrganizationFollowingRepository;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserOrganizationFollowingObject> getByFilter(
            @RequestParam(value = "user_id", required = false) String userId,
            @RequestParam(value = "org_id", required = false) String orgId,
            Pageable pageable,
            HttpServletResponse response) {
        Page<UserOrganizationFollowingEntity> entityPage = null;
        if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(orgId)) {
            return userOrganizationFollowingRepository.findByUserIdAndOrgId(userId, orgId)
                    .stream()
                    .map(this::toUserOrganizationFollowingObject)
                    .collect(Collectors.toList());
        }
        else if (!StringUtils.isEmpty(userId)){
            entityPage = userOrganizationFollowingRepository.findByUserId(userId, pageable);
        } else if (!StringUtils.isEmpty(orgId)) {
            entityPage = userOrganizationFollowingRepository.findByOrgId(orgId, pageable);
        }

        if( entityPage != null) {
            if (pageable != null) {
                response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
            }
            return entityPage.getContent().stream().map(this::toUserOrganizationFollowingObject).collect(Collectors.toList());
        }
        else{
            throw new BadRequestException("at least one parameter need to be specified: user_id, org_id");
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserOrganizationFollowingObject create(@RequestBody @Valid UserOrganizationFollowingObject userOrganizationFollowingObject) {
        UserOrganizationFollowingEntity entity = toUserOrganizationFollowingEntity(userOrganizationFollowingObject);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        UserOrganizationFollowingEntity newEntity = userOrganizationFollowingRepository.save(entity);
        return toUserOrganizationFollowingObject(newEntity);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByUserIdAndOrgId(@RequestParam(value = "user_id", required = true) String userId,
                                       @RequestParam(value = "org_id", required = true) String orgId) {
        userOrganizationFollowingRepository.deleteByUserIdAndOrgId(userId, orgId);
    }


    private UserOrganizationFollowingEntity toUserOrganizationFollowingEntity(UserOrganizationFollowingObject object) {
        if (object == null) {
            return null;
        }
        UserOrganizationFollowingEntity entity = new UserOrganizationFollowingEntity();
        entity.setId(object.getId());
        entity.setUserId(object.getUserId());
        entity.setOrgId(object.getOrgId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }


    private UserOrganizationFollowingObject toUserOrganizationFollowingObject(UserOrganizationFollowingEntity entity) {
        if (entity == null) {
            return null;
        }
        UserOrganizationFollowingObject object = new UserOrganizationFollowingObject();
        object.setId(entity.getId());
        object.setUserId(entity.getUserId());
        object.setOrgId(entity.getOrgId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        return object;
    }

}