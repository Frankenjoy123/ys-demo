package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserConfigObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.UserConfigEntity;
import com.yunsoo.data.service.repository.UserConfigRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by:   Lijian
 * Created on:   2015/9/16
 * Descriptions:
 */
@RestController
@RequestMapping("/userConfig")
public class UserConfigController {

    @Autowired
    private UserConfigRepository userConfigRepository;

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public UserConfigObject getUserConfigObjectByUserId(@PathVariable("userId") String userId) {
        UserConfigEntity entity = userConfigRepository.findOne(userId);
        if (entity == null) {
            throw new NotFoundException("user config not found by [user_id: " + userId + "]");
        }
        return toUserConfigObject(entity);
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.PUT)
    public UserConfigObject saveUserConfigObject(@PathVariable("userId") String userId,
                                                 @RequestBody UserConfigObject userConfigObject) {
        userConfigObject.setUserId(userId);
        UserConfigEntity entity = toUserConfigEntity(userConfigObject);
        if (entity.getModifiedDateTime() == null) {
            entity.setModifiedDateTime(DateTime.now());
        }
        entity = userConfigRepository.save(entity);
        return toUserConfigObject(entity);
    }


    private UserConfigObject toUserConfigObject(UserConfigEntity entity) {
        if (entity == null) {
            return null;
        }
        UserConfigObject object = new UserConfigObject();
        object.setUserId(entity.getUserId());
        object.setAutoFollowing(entity.isAutoFollowing());
        object.setModifiedDateTime(entity.getModifiedDateTime());
        return object;
    }

    private UserConfigEntity toUserConfigEntity(UserConfigObject object) {
        if (object == null) {
            return null;
        }
        UserConfigEntity entity = new UserConfigEntity();
        entity.setUserId(object.getUserId());
        entity.setAutoFollowing(object.isAutoFollowing());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        return entity;
    }
}
