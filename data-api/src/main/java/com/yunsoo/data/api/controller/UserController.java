package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.UserEntity;
import com.yunsoo.data.service.repository.UserRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
                                        @RequestParam(value = "open_id", required = false) String openId,
                                        @RequestParam(value = "oauth_type", required = false) String oauthType,
                                        Pageable pageable,
                                        HttpServletResponse response) {
        Page<UserEntity> entityPage;
        if (idIn != null && idIn.size() > 0) {
            entityPage = userRepository.findByIdIn(idIn, pageable);
        } else if (!StringUtils.isEmpty(phone)) {
            entityPage = userRepository.findByPhone(phone, pageable);
        } else if (!StringUtils.isEmpty(deviceId)) {
            entityPage = userRepository.findByDeviceId(deviceId, pageable);
        } else if (!StringUtils.isEmpty(openId)) {
            entityPage = userRepository.findByOauthOpenidAndOauthTypeCode(openId, oauthType, pageable);
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

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public List<UserObject> queryUser(@RequestParam(value = "sex", required = false) Boolean sex,
                                      @RequestParam(value = "phone", required = false) String phone,
                                      @RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "province", required = false) String province,
                                      @RequestParam(value = "city", required = false) String city,
                                      @RequestParam(value = "age_start", required = false) Integer ageStart,
                                      @RequestParam(value = "age_end", required = false) Integer ageEnd,
                                      @RequestParam(value = "create_datetime_start", required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                      @RequestParam(value = "create_datetime_end", required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                      Pageable pageable,
                                      HttpServletResponse response) {

        DateTime createdDateTimeStartTo = null;
        DateTime createdDateTimeEndTo = null;

        if (createdDateTimeStart != null && !StringUtils.isEmpty(createdDateTimeStart.toString()))
            createdDateTimeStartTo = createdDateTimeStart.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));

        if (createdDateTimeEnd != null && !StringUtils.isEmpty(createdDateTimeEnd.toString()))
            createdDateTimeEndTo = createdDateTimeEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);

        Page<UserEntity> entityPage = userRepository.findByFilter(sex, phone, name, province, city, ageStart, ageEnd, createdDateTimeStartTo, createdDateTimeEndTo, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream()
                .map(this::toUserObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserObject create(@RequestBody @Valid UserObject userObject) {
        UserEntity entity = toUserEntity(userObject);
        DateTime now = DateTime.now();

        //check phone
        if (entity.getPhone() != null) {
            checkPhoneExists(entity.getPhone());
        }

        entity.setId(null);
        if (entity.getStatusCode() == null) {
            entity.setStatusCode(LookupCodes.UserStatus.ENABLED);
        }
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
                checkPhoneExists(newPhone);
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
        if (userObject.getOauthOpenid() != null) {
            entity.setOauthOpenid(userObject.getOauthOpenid());
        }
        if (userObject.getAge() != null) {
            entity.setAge(userObject.getAge());
        }
        if (userObject.getSex() != null) {
            entity.setSex(userObject.getSex());
        }
        if (userObject.getEmail() != null) {
            entity.setEmail(userObject.getEmail());
        }
        if (userObject.getGravatarUrl() != null) {
            entity.setGravatarUrl(userObject.getGravatarUrl());
        }
        if (userObject.getCity() != null) {
            entity.setCity(userObject.getCity());
        }
        if (userObject.getOauthTypeCode() != null) {
            entity.setOauthTypeCode(userObject.getOauthTypeCode());
        }
        if (userObject.getProvince() != null) {
            entity.setProvince(userObject.getProvince());
        }
        if (userObject.getModifiedDateTime() != null) {
            entity.setModifiedDateTime(userObject.getModifiedDateTime());
        }

        userRepository.save(entity);
    }

    @RequestMapping(value = "oauth", method = RequestMethod.POST)
    public UserObject getOrCreateUserByOauthOpenId(@RequestBody UserObject userObject) {
        if (StringUtils.isEmpty(userObject.getOauthOpenid()) || StringUtils.isEmpty(userObject.getOauthTypeCode())) {
            throw new BadRequestException();
        }
        List<UserEntity> entities = userRepository.findByOauthOpenidAndOauthTypeCode(userObject.getOauthOpenid(), userObject.getOauthTypeCode(), null).getContent();
        if (entities.size() > 0) {
            UserEntity entity = entities.get(0);
            if (!StringUtils.isEmpty(userObject.getName())) {
                entity.setName(userObject.getName());
                if (userObject.getSex() != null) {
                    entity.setSex(userObject.getSex());
                }
                if (userObject.getProvince() != null) {
                    entity.setProvince(userObject.getProvince());
                }
                if (userObject.getCity() != null) {
                    entity.setCity(userObject.getCity());
                }
                if (userObject.getGravatarUrl() != null) {
                    entity.setGravatarUrl(userObject.getGravatarUrl());
                }
                entity = userRepository.save(entity);
            }
            return toUserObject(entity);
        } else {
            return create(userObject);
        }
    }

    private UserEntity findUserById(String id) {
        UserEntity entity = userRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("User not found by [id: " + id + "]");
        }
        return entity;
    }

    private void checkPhoneExists(String phone) {
        if (userRepository.findByPhone(phone).size() > 0) {
            throw new ConflictException("phone already registered by another user");
        }
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
        object.setOauthOpenid(entity.getOauthOpenid());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setAge(entity.getAge());
        object.setSex(entity.getSex());
        object.setEmail(entity.getEmail());
        object.setGravatarUrl(entity.getGravatarUrl());
        object.setCity(entity.getCity());
        object.setOauthTypeCode(entity.getOauthTypeCode());
        object.setProvince(entity.getProvince());
        object.setModifiedDateTime(entity.getModifiedDateTime());
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
        entity.setOauthOpenid(object.getOauthOpenid());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setAge(object.getAge());
        entity.setSex(object.getSex());
        entity.setEmail(object.getEmail());
        entity.setGravatarUrl(object.getGravatarUrl());
        entity.setCity(object.getCity());
        entity.setOauthTypeCode(object.getOauthTypeCode());
        entity.setProvince(object.getProvince());
        entity.setModifiedDateTime(object.getModifiedDateTime());
        return entity;
    }

}
