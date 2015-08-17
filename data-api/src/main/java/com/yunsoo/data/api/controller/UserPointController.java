package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserPointObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.UserPointEntity;
import com.yunsoo.data.service.repository.UserPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "{userId}", method = RequestMethod.PUT)
    public void createOrUpdateByUserId(@PathVariable("userId") String userId, @RequestBody UserPointObject userPoint) {
        if (userPoint.getPoint() == null) {
            throw new BadRequestException("point must not be null");
        }
        if (userPoint.getLastSignInDateTime() == null) {
            throw new BadRequestException("last_sign_in_datetime must not be null");
        }
        if (userPoint.getContinuousSignInDays() == null) {
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
        if (userPoint.getContinuousSignInDays() != null) {
            entity.setContinuousSignInDays(userPoint.getContinuousSignInDays());
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
        object.setContinuousSignInDays(entity.getContinuousSignInDays());
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
        entity.setContinuousSignInDays(object.getContinuousSignInDays());
        return entity;
    }

}
