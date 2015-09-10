package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserProductFollowingObject;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.UserProductFollowingEntity;
import com.yunsoo.data.service.repository.UserProductFollowingRepository;
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
 * Created by:   yan
 * Created on:   8/17/2015
 * Descriptions:
 */
@RestController
@RequestMapping("/UserProductFollowing")
public class UserProductFollowingController {

    @Autowired
    private UserProductFollowingRepository userProductFollowingRepository;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserProductFollowingObject> getByFilters(
            @RequestParam(value = "user_id", required = false) String userId,
            @RequestParam(value = "product_base_id", required = false) String productBaseId,
            Pageable pageable,
            HttpServletResponse response) {

        Page<UserProductFollowingEntity> entityPage = null;
        if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(productBaseId)) {
            return userProductFollowingRepository.findByUserIdAndProductBaseId(userId, productBaseId).stream()
                    .map(this::toUserProductFollowingObject)
                    .collect(Collectors.toList());
        }
        else if (!StringUtils.isEmpty(userId)){
            entityPage = userProductFollowingRepository.findByUserId(userId, pageable);
        } else if (!StringUtils.isEmpty(productBaseId)) {
            entityPage = userProductFollowingRepository.findByProductBaseId(productBaseId, pageable);
        }

        if (pageable != null && entityPage != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }
        return entityPage.getContent().stream().map(this::toUserProductFollowingObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserProductFollowingObject create(@RequestBody @Valid UserProductFollowingObject object) {
        UserProductFollowingEntity entity = toUserProductFollowingEntity(object);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        UserProductFollowingEntity newEntity = userProductFollowingRepository.save(entity);
        return toUserProductFollowingObject(newEntity);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByUserIdAndProductBaseId(@RequestParam(value = "user_id", required = true) String userId,
                                               @RequestParam(value = "product_base_id", required = true) String productBaseId) {
        userProductFollowingRepository.deleteByUserIdAndProductBaseId(userId, productBaseId);
    }

    @RequestMapping(value = "count", method = RequestMethod.GET)
    public Long countByProductBaseId(@RequestParam(value = "product_base_id", required = true) String productBaseId) {
        return userProductFollowingRepository.countByProductBaseId(productBaseId);
    }


    private UserProductFollowingEntity toUserProductFollowingEntity(UserProductFollowingObject object) {
        if (object == null) {
            return null;
        }
        UserProductFollowingEntity entity = new UserProductFollowingEntity();
        entity.setId(object.getId());
        entity.setUserId(object.getUserId());
        entity.setProductBaseId(object.getProductBaseId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }

    private UserProductFollowingObject toUserProductFollowingObject(UserProductFollowingEntity entity) {
        if (entity == null) {
            return null;
        }
        UserProductFollowingObject object = new UserProductFollowingObject();
        object.setId(entity.getId());
        object.setUserId(entity.getUserId());
        object.setProductBaseId(entity.getProductBaseId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        return object;
    }

}
