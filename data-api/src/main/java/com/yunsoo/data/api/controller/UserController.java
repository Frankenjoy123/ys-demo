package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.UserEntity;
import com.yunsoo.data.service.repository.UserRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserObject> getByFilter(@RequestParam(value = "id_in", required = false) List<String> idIn,
                                        @RequestParam(value = "phone", required = false) String phone,
                                        @RequestParam(value = "device_id", required = false) String deviceId,
                                        @RequestParam(value = "point_ge", required = false) Integer pointGE,
                                        @RequestParam(value = "point_le", required = false) Integer pointLE,
                                        Pageable pageable,
                                        HttpServletResponse response) {
        Page<UserEntity> entityPage;
        if (idIn != null && idIn.size() > 0) {
            entityPage = userRepository.findByIdIn(idIn, pageable);
        } else if (!StringUtils.isEmpty(phone)) {
            entityPage = userRepository.findByPhone(phone, pageable);
        } else if (!StringUtils.isEmpty(deviceId)) {
            entityPage = userRepository.findByDeviceId(deviceId, pageable);
        } else if (pointGE != null || pointLE != null) {
            entityPage = userRepository.query(pointGE, pointLE, pageable);
        } else {
            throw new BadRequestException("at least need one filter parameter [id_in, phone, device_id, point_ge, point_le]");
        }
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }
        return entityPage.getContent().stream().map(this::toUserObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public UserObject create(@RequestBody @Valid UserObject userObject) {
        UserEntity entity = toUserEntity(userObject);
        DateTime now = DateTime.now();
        entity.setId(null);
        if (entity.getPoint() == null) {
            entity.setPoint(0);
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
        object.setCreatedDateTime(entity.getCreatedDateTime());
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
        entity.setCreatedDateTime(object.getCreatedDateTime());
        return entity;
    }

}
