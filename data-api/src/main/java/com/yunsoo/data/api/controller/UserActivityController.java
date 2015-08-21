package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserActivityObject;
import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.UserActivityEntity;
import com.yunsoo.data.service.repository.UserActivityRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/8/17
 * Descriptions:
 */
@RestController
@RequestMapping("/useractivity")
public class UserActivityController {

    @Autowired
    private UserActivityRepository userActivityRepository;


    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public UserActivityObject getByUserId(@PathVariable("userId") String userId) {
        UserActivityEntity entity = userActivityRepository.findOne(userId);
        if (entity == null) {
            throw new NotFoundException("user activity not found by [user_id: " + userId + "]");
        }
        return toUserPointObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserActivityObject> getByFilter(
            @RequestParam(value = "last_sign_in_datetime_ge", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            DateTime lastSignInDatetimeGE,
            @RequestParam(value = "last_sign_in_datetime_le", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            DateTime lastSignInDatetimeLE,
            @RequestParam(value = "last_sign_in_continuous_days_ge", required = false)
            Integer lastSignInContinuousDaysGE,
            @RequestParam(value = "last_sign_in_continuous_days_le", required = false)
            Integer lastSignInContinuousDaysLE,
            Pageable pageable,
            HttpServletResponse response) {
        Page<UserActivityEntity> entityPage = userActivityRepository.query(
                DateTimeUtils.toDBString(lastSignInDatetimeGE),
                DateTimeUtils.toDBString(lastSignInDatetimeLE),
                lastSignInContinuousDaysGE,
                lastSignInContinuousDaysLE,
                pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }
        return entityPage.getContent().stream().map(this::toUserPointObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.PUT)
    public void createOrUpdateByUserId(@PathVariable("userId") String userId, @RequestBody UserActivityObject userPoint) {
        UserActivityEntity entity = toUserPointEntity(userPoint);
        entity.setUserId(userId);
        userActivityRepository.save(entity);
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.PATCH)
    public void patchUpdateByUserId(@PathVariable("userId") String userId, @RequestBody UserActivityObject userPoint) {
        UserActivityEntity entity = userActivityRepository.findOne(userId);
        if (entity == null) {
            entity = new UserActivityEntity();
            entity.setUserId(userId);
        }
        if (userPoint.getLastSignInDateTime() != null) {
            entity.setLastSignInDateTime(userPoint.getLastSignInDateTime());
        }
        if (userPoint.getLastSignInContinuousDays() != null) {
            entity.setLastSignInContinuousDays(userPoint.getLastSignInContinuousDays());
        }
        userActivityRepository.save(entity);
    }

    private UserActivityObject toUserPointObject(UserActivityEntity entity) {
        if (entity == null) {
            return null;
        }
        UserActivityObject object = new UserActivityObject();
        object.setUserId(entity.getUserId());
        object.setLastSignInDateTime(entity.getLastSignInDateTime());
        object.setLastSignInContinuousDays(entity.getLastSignInContinuousDays());
        return object;
    }

    private UserActivityEntity toUserPointEntity(UserActivityObject object) {
        if (object == null) {
            return null;
        }
        UserActivityEntity entity = new UserActivityEntity();
        entity.setUserId(object.getUserId());
        entity.setLastSignInDateTime(object.getLastSignInDateTime());
        entity.setLastSignInContinuousDays(object.getLastSignInContinuousDays());
        return entity;
    }

}
