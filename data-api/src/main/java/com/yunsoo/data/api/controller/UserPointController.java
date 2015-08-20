package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserPointObject;
import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.UserPointEntity;
import com.yunsoo.data.service.repository.UserPointRepository;
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
@RequestMapping("/userpoint")
public class UserPointController {

    @Autowired
    private UserPointRepository userPointRepository;


    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public UserPointObject getByUserId(@PathVariable("userId") String userId) {
        UserPointEntity entity = userPointRepository.findOne(userId);
        if (entity == null) {
            throw new NotFoundException("user point not found by [user_id: " + userId + "]");
        }
        return toUserPointObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserPointObject> getByFilter(@RequestParam(value = "point_ge", required = false) Integer pointGE,
                                             @RequestParam(value = "point_le", required = false) Integer pointLE,
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
        Page<UserPointEntity> entityPage = userPointRepository.query(
                pointGE,
                pointLE,
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
    public void createOrUpdateByUserId(@PathVariable("userId") String userId, @RequestBody UserPointObject userPoint) {
        if (userPoint.getPoint() == null) {
            throw new BadRequestException("point must not be null");
        }
        if (userPoint.getLastSignInDateTime() == null) {
            throw new BadRequestException("last_sign_in_datetime must not be null");
        }
        if (userPoint.getLastSignInContinuousDays() == null) {
            throw new BadRequestException("continuous_sign_in_days must not be null");
        }
        UserPointEntity entity = toUserPointEntity(userPoint);
        entity.setUserId(userId);
        userPointRepository.save(entity);
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.PATCH)
    public void patchUpdateByUserId(@PathVariable("userId") String userId, @RequestBody UserPointObject userPoint) {
        UserPointEntity entity = userPointRepository.findOne(userId);
        if (entity == null) {
            throw new NotFoundException("user point not found by [user_id: " + userId + "]");
        }
        if (userPoint.getPoint() != null) {
            entity.setPoint(userPoint.getPoint());
        }
        if (userPoint.getLastSignInDateTime() != null) {
            entity.setLastSignInDateTime(userPoint.getLastSignInDateTime());
        }
        if (userPoint.getLastSignInContinuousDays() != null) {
            entity.setLastSignInContinuousDays(userPoint.getLastSignInContinuousDays());
        }
        userPointRepository.save(entity);
    }

    private UserPointObject toUserPointObject(UserPointEntity entity) {
        if (entity == null) {
            return null;
        }
        UserPointObject object = new UserPointObject();
        object.setUserId(entity.getUserId());
        object.setPoint(entity.getPoint());
        object.setLastSignInDateTime(entity.getLastSignInDateTime());
        object.setLastSignInContinuousDays(entity.getLastSignInContinuousDays());
        return object;
    }

    private UserPointEntity toUserPointEntity(UserPointObject object) {
        if (object == null) {
            return null;
        }
        UserPointEntity entity = new UserPointEntity();
        entity.setUserId(object.getUserId());
        entity.setPoint(object.getPoint());
        entity.setLastSignInDateTime(object.getLastSignInDateTime());
        entity.setLastSignInContinuousDays(object.getLastSignInContinuousDays());
        return entity;
    }

}
