package com.yunsoo.di.api.controller;

import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.di.dao.entity.EMRUserEntity;
import com.yunsoo.di.dao.repository.CustomerEventRepository;
import com.yunsoo.di.dao.repository.EMRUserRepository;
import com.yunsoo.di.dto.EMRUserObject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
 * Created by xiaowu on 2016/12/23.
 */
@RestController
@RequestMapping("/emr/user")
public class EMRUserController {

    @Autowired
    private EMRUserRepository emrUserRepository;

    @Autowired
    private CustomerEventRepository customerEventRepository;

    // 消费者行为分析，扫描用户列表
    @RequestMapping(value = "/scan", method = RequestMethod.GET)
    public List<EMRUserObject> findEventUsersFilterByScan(@RequestParam(value = "org_id", required = false) String orgId,
                                                          @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                          @RequestParam(value = "province", required = false) String province,
                                                          @RequestParam(value = "city", required = false) String city,
                                                          @RequestParam(value = "create_datetime_start", required = false)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                                          @RequestParam(value = "create_datetime_end", required = false)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                                          @RequestParam(value = "wx_user", required = false) Boolean wxUser,
                                                          Pageable pageable,
                                                          HttpServletResponse response) {

        DateTime createdDateTimeStartTo = null;
        DateTime createdDateTimeEndTo = null;

        if (createdDateTimeStart != null && !StringUtils.isEmpty(createdDateTimeStart.toString()))
            createdDateTimeStartTo = createdDateTimeStart.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));

        if (createdDateTimeEnd != null && !StringUtils.isEmpty(createdDateTimeEnd.toString()))
            createdDateTimeEndTo = createdDateTimeEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        List<EMRUserEntity> list = emrUserRepository.findEventUsersFilterByScan(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, wxUser,pageable);

        int totalCount = (customerEventRepository.scanCount(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, wxUser))[1];

        Page<EMRUserEntity> entityPage = new PageImpl<EMRUserEntity>(list.stream().collect(Collectors.toList()),
                pageable, totalCount);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream().map(this::toEMRUserObject).collect(Collectors.toList());
    }

    // 消费者行为分析，抽奖用户列表
    @RequestMapping(value = "/draw", method = RequestMethod.GET)
    public List<EMRUserObject> findEventUsersFilterByDraw(@RequestParam(value = "org_id", required = false) String orgId,
                                                          @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                          @RequestParam(value = "province", required = false) String province,
                                                          @RequestParam(value = "city", required = false) String city,
                                                          @RequestParam(value = "create_datetime_start", required = false)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                                          @RequestParam(value = "create_datetime_end", required = false)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                                          @RequestParam(value = "wx_user", required = false) Boolean wxUser,
                                                          Pageable pageable,
                                                          HttpServletResponse response) {

        DateTime createdDateTimeStartTo = null;
        DateTime createdDateTimeEndTo = null;

        if (createdDateTimeStart != null && !StringUtils.isEmpty(createdDateTimeStart.toString()))
            createdDateTimeStartTo = createdDateTimeStart.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));

        if (createdDateTimeEnd != null && !StringUtils.isEmpty(createdDateTimeEnd.toString()))
            createdDateTimeEndTo = createdDateTimeEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        List<EMRUserEntity> list = emrUserRepository.findEventUsersFilterByDraw(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, wxUser, pageable);

        int totalCount = (customerEventRepository.drawCount(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, wxUser))[1];

        Page<EMRUserEntity> entityPage = new PageImpl<EMRUserEntity>(list.stream().collect(Collectors.toList()),
                pageable, totalCount);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }
        return entityPage.getContent().stream().map(this::toEMRUserObject).collect(Collectors.toList());
    }

    // 消费者行为分析，中奖用户列表
    @RequestMapping(value = "/win", method = RequestMethod.GET)
    public List<EMRUserObject> findEventUsersFilterByWin(@RequestParam(value = "org_id", required = false) String orgId,
                                                         @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                         @RequestParam(value = "province", required = false) String province,
                                                         @RequestParam(value = "city", required = false) String city,
                                                         @RequestParam(value = "create_datetime_start", required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                                         @RequestParam(value = "create_datetime_end", required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                                         @RequestParam(value = "wx_user", required = false) Boolean wxUser,
                                                         Pageable pageable,
                                                         HttpServletResponse response) {

        DateTime createdDateTimeStartTo = null;
        DateTime createdDateTimeEndTo = null;

        if (createdDateTimeStart != null && !StringUtils.isEmpty(createdDateTimeStart.toString()))
            createdDateTimeStartTo = createdDateTimeStart.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));

        if (createdDateTimeEnd != null && !StringUtils.isEmpty(createdDateTimeEnd.toString()))
            createdDateTimeEndTo = createdDateTimeEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        List<EMRUserEntity> list = emrUserRepository.findEventUsersFilterByWin(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, wxUser, pageable);

        int totalCount = (customerEventRepository.winCount(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, wxUser))[1];

        Page<EMRUserEntity> entityPage = new PageImpl<EMRUserEntity>(list.stream().collect(Collectors.toList()),
                pageable, totalCount);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }
        return entityPage.getContent().stream().map(this::toEMRUserObject).collect(Collectors.toList());
    }

    // 消费者行为分析，兑奖用户列表
    @RequestMapping(value = "/reward", method = RequestMethod.GET)
    public List<EMRUserObject> findEventUsersFilterByReward(@RequestParam(value = "org_id", required = false) String orgId,
                                                            @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                            @RequestParam(value = "province", required = false) String province,
                                                            @RequestParam(value = "city", required = false) String city,
                                                            @RequestParam(value = "create_datetime_start", required = false)
                                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                                            @RequestParam(value = "create_datetime_end", required = false)
                                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                                            @RequestParam(value = "wx_user", required = false) Boolean wxUser,
                                                            Pageable pageable,
                                                            HttpServletResponse response) {

        DateTime createdDateTimeStartTo = null;
        DateTime createdDateTimeEndTo = null;

        if (createdDateTimeStart != null && !StringUtils.isEmpty(createdDateTimeStart.toString()))
            createdDateTimeStartTo = createdDateTimeStart.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));

        if (createdDateTimeEnd != null && !StringUtils.isEmpty(createdDateTimeEnd.toString()))
            createdDateTimeEndTo = createdDateTimeEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        List<EMRUserEntity> list = emrUserRepository.findEventUsersFilterByReward(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, wxUser, pageable);

        int totalCount = (customerEventRepository.rewardCount(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, wxUser))[1];

        Page<EMRUserEntity> entityPage = new PageImpl<EMRUserEntity>(list.stream().collect(Collectors.toList()),
                pageable, totalCount);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }
        return entityPage.getContent().stream().map(this::toEMRUserObject).collect(Collectors.toList());
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
        object.setOrgName(entity.getOrgName());
        object.setName(entity.getName());
        object.setPhone(entity.getPhone());
        object.setAddress(entity.getAddress());
        object.setWxOpenid(entity.getWxOpenId());
        object.setAge(entity.getAge());
        object.setSex(entity.getSex());
        object.setEmail(entity.getEmail());
        object.setGravatarUrl(entity.getGravatarUrl());
        object.setCity(entity.getCity());
        object.setProvince(entity.getProvince());
        object.setJoinDateTime(entity.getJoinDateTime());
        object.setIp(entity.getIp());
        object.setDevice(entity.getDevice());
        return object;
    }
}
