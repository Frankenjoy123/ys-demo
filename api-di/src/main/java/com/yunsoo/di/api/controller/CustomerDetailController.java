package com.yunsoo.di.api.controller;

import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.di.dao.entity.EMREventEntity;
import com.yunsoo.di.dao.entity.EMRUserEntity;
import com.yunsoo.di.dao.entity.UserTagEntity;
import com.yunsoo.di.dao.repository.EMREventRepository;
import com.yunsoo.di.dao.repository.EMRUserRepository;
import com.yunsoo.di.dao.repository.UserTagRepository;
import com.yunsoo.di.dto.EMREventObject;
import com.yunsoo.di.dto.EMRUserObject;
import com.yunsoo.di.dto.EMRUserProductEventStasticsObject;
import com.yunsoo.di.dto.UserTagObject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   xiaowu
 * Created on:   2016/11/3
 * Descriptions: customer detail  api
 */
@RestController
@RequestMapping("/user")
public class CustomerDetailController {

    @Autowired
    private EMRUserRepository emrUserRepository;

    @Autowired
    private UserTagRepository userTagRepository;

    @Autowired
    private EMREventRepository emrEventRepository;

    @RequestMapping(value = "id", method = RequestMethod.GET)
    public EMRUserObject getUser(@RequestParam(value = "org_id", required = true) String orgId,
                                 @RequestParam(value = "user_id", required = false) String userId,
                                 @RequestParam(value = "ys_id", required = false) String ysId) {

        EMRUserEntity entity = emrUserRepository.getUser(orgId, userId, ysId);

        EMRUserObject object = toEMRUserObject(entity);
        List<UserTagEntity> userTagEntities = userTagRepository.findByFilter(userId, ysId, orgId);
        List<UserTagObject> userTagObjects = userTagEntities.stream().map(this::toUserTagObject).collect(Collectors.toList());
        object.setUserTagObjects(userTagObjects);

        return object;
    }


    private EMRUserObject toEMRUserObject(EMRUserEntity entity) {
        if (entity == null) {
            return null;
        }
        EMRUserObject object = new EMRUserObject();
        object.setId(entity.getId());
        object.setUserId(entity.getUserId());
        object.setYsId(entity.getYsId());
        object.setOrgId(entity.getOrgId());
        object.setName(entity.getName());
        object.setProvince(entity.getProvince());
        object.setCity(entity.getCity());
        object.setPhone(entity.getPhone());
        object.setWxOpenid(entity.getWxOpenId());
        object.setAge(entity.getAge());
        object.setSex(entity.getSex());
        object.setEmail(entity.getEmail());
        object.setGravatarUrl(entity.getGravatarUrl());
        object.setJoinDateTime(entity.getJoinDateTime());
        object.setIp(entity.getIp());
        object.setDevice(entity.getDevice());
        return object;
    }

    private UserTagObject toUserTagObject(UserTagEntity entity) {
        if (entity == null) {
            return null;
        }

        UserTagObject object = new UserTagObject();
        object.setId(entity.getId());
        object.setUserId(entity.getUserId());
        object.setYsId(entity.getYsId());
        object.setOrgId(entity.getOrgId());
        object.setTagId(entity.getTagId());
        object.setTagName(entity.getTagName());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());

        return object;
    }

}
