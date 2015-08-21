package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.UserEntity;
import com.yunsoo.data.service.repository.UserRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Zhe
 * Created on:   2015/1/26
 * Descriptions:
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public UserObject getById(@PathVariable(value = "id") String id) {
        UserEntity entity = findUserById(id);
        return toUserObject(entity);
    }

    @RequestMapping(value = "multiple/{ids}", method = RequestMethod.GET)
    public List<UserObject> getByIds(@PathVariable(value = "ids")String[] ids) {
        List<UserEntity> entities = userRepository.findByIdIn(Arrays.asList(ids));
        return entities.stream().map(this::toUserObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserObject> getByFilter(@RequestParam(value = "phone", required = false) String phone,
                                        @RequestParam(value = "device_id", required = false) String deviceId) {
        List<UserEntity> entities;
        if (!StringUtils.isEmpty(phone)) {
            entities = userRepository.findByPhone(phone);
        } else if (!StringUtils.isEmpty(deviceId)) {
            entities = userRepository.findByDeviceId(deviceId);
        } else {
            throw new BadRequestException("at least need one filter parameter of phone or device_id");
        }
        if (entities != null) {
            return entities.stream().map(this::toUserObject).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public UserObject create(@RequestBody @Valid UserObject userObject) {
        UserEntity entity = toUserEntity(userObject);
        DateTime now = DateTime.now();
        entity.setId(null);
        if (entity.getLastAccessDateTime() == null) {
            entity.setLastAccessDateTime(now);
        }
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(now);
        }
        UserEntity newEntity = userRepository.save(entity);
        return toUserObject(newEntity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable(value = "id") String id, @RequestBody UserObject userObject) {
        UserEntity entity = findUserById(id);
        String newPhone = userObject.getPhone();
        if (newPhone != null) {
            if (!newPhone.equals(entity.getPhone())) {
                if (userRepository.findByPhone(newPhone).size() > 0) {
                    throw new ConflictException("phone already used by another user");
                }
                entity.setPhone(userObject.getPhone());
            }
        }
        if (userObject.getDeviceId() != null) {
            entity.setDeviceId(userObject.getDeviceId());
        }
        if (userObject.getName() != null) {
            entity.setName(userObject.getName());
        }
        if (userObject.getStatusCode() != null) {
            entity.setStatusCode(userObject.getStatusCode());
        }
        if (userObject.getPoint() != null) {
            entity.setPoint(userObject.getPoint());
        }
        if (userObject.getAddress() != null) {
            entity.setAddress(userObject.getAddress());
        }
        if (userObject.getLastAccessDatetime() != null) {
            entity.setLastAccessDateTime(userObject.getLastAccessDatetime());
        }
        userRepository.save(entity);
    }

    private UserEntity findUserById(String id) {
        UserEntity entity = userRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("User not found by [id: " + id + "]");
        }
        return entity;
    }

    private UserObject toUserObject(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        UserObject object = new UserObject();
        object.setId(entity.getId());
        object.setDeviceId(entity.getDeviceId());
        object.setPhone(entity.getPhone());
        object.setName(entity.getName());
        object.setStatusCode(entity.getStatusCode());
        object.setPoint(entity.getPoint());
        object.setAddress(entity.getAddress());
        object.setLastAccessDatetime(entity.getLastAccessDateTime());
        object.setCreatedDatetime(entity.getCreatedDateTime());
        return object;
    }

    private UserEntity toUserEntity(UserObject object) {
        if (object == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(object.getId());
        entity.setDeviceId(object.getDeviceId());
        entity.setPhone(object.getPhone());
        entity.setName(object.getName());
        entity.setStatusCode(object.getStatusCode());
        entity.setPoint(object.getPoint());
        entity.setAddress(object.getAddress());
        entity.setLastAccessDateTime(object.getLastAccessDatetime());
        entity.setCreatedDateTime(object.getCreatedDatetime());
        return entity;
    }

}
