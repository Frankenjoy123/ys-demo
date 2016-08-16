package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.EMRUserObject;
import com.yunsoo.common.data.object.EMRUserProductEventStasticsObject;
import com.yunsoo.common.data.object.UserTagObject;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.EMRUserEntity;
import com.yunsoo.data.service.entity.EMRUserProductEventStatistics;
import com.yunsoo.data.service.entity.UserTagEntity;
import com.yunsoo.data.service.repository.EMRUserRepository;
import com.yunsoo.data.service.repository.UserTagRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/emr/user")
public class EMRUserController {

    @Autowired
    private EMRUserRepository emrUserRepository;

    @Autowired
    private UserTagRepository userTagRepository;

    @RequestMapping(value = "id", method = RequestMethod.GET)
    public EMRUserObject getUser(@RequestParam(value = "org_id", required = false) String orgId,
                                 @RequestParam(value = "user_id", required = false) String userId,
                                 @RequestParam(value = "ys_id", required = false) String ysId) {

        EMRUserEntity entity = emrUserRepository.getUser(orgId, userId, ysId);

        EMRUserObject object = toEMRUserObject(entity);
        List<UserTagEntity> userTagEntities = userTagRepository.findByFilter(userId, ysId, orgId);
        List<UserTagObject> userTagObjects = userTagEntities.stream().map(this::toUserTagObject).collect(Collectors.toList());
        object.setUserTagObjects(userTagObjects);

        return object;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<EMRUserObject> findByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                            @RequestParam(value = "sex", required = false) Boolean sex,
                                            @RequestParam(value = "wx_user", required = false) Boolean wxUser,
                                            @RequestParam(value = "phone", required = false) String phone,
                                            @RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "province", required = false) String province,
                                            @RequestParam(value = "city", required = false) String city,
                                            @RequestParam(value = "age_start", required = false) Integer ageStart,
                                            @RequestParam(value = "age_end", required = false) Integer ageEnd,
                                            @RequestParam(value = "user_tags", required = false) String userTags,
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

        List<String> tags;
        if (userTags != null && !StringUtils.isEmpty(userTags)) {
            tags = Arrays.asList(userTags.split(","));
        } else {
            tags = null;
        }

        Page<EMRUserEntity> entityPage = emrUserRepository.findByFilter(orgId, sex, phone, name, province, city, ageStart, ageEnd, createdDateTimeStartTo, createdDateTimeEndTo,
                tags == null || tags.size() == 0 ? null : tags,
                tags == null || tags.size() == 0, wxUser, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream()
                .map(this::toEMRUserObject)
                .collect(Collectors.toList());
    }


    @RequestMapping(value = "/scan", method = RequestMethod.GET)
    public List<EMRUserObject> findEventUsersFilterByScan(@RequestParam(value = "org_id", required = false) String orgId,
                                                          @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                          @RequestParam(value = "province", required = false) String province,
                                                          @RequestParam(value = "city", required = false) String city,
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

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        List<EMRUserEntity> list = emrUserRepository.findEventUsersFilterByScan(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, pageable);

        int totalCount = emrUserRepository.countEventUsersFilterByScan(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo);

        Page<EMRUserEntity> entityPage = new PageImpl<EMRUserEntity>(list.stream().collect(Collectors.toList()),
                pageable, totalCount);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream().map(this::toEMRUserObject).collect(Collectors.toList());
    }


    @RequestMapping(value = "/draw", method = RequestMethod.GET)
    public List<EMRUserObject> findEventUsersFilterByDraw(@RequestParam(value = "org_id", required = false) String orgId,
                                                          @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                          @RequestParam(value = "province", required = false) String province,
                                                          @RequestParam(value = "city", required = false) String city,
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

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        Page<EMRUserEntity> entityPage = emrUserRepository.findEventUsersFilterByDraw(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream()
                .map(this::toEMRUserObject)
                .collect(Collectors.toList());
    }


    @RequestMapping(value = "/wx", method = RequestMethod.GET)
    public List<EMRUserObject> findEventUsersFilterByWX(@RequestParam(value = "org_id", required = false) String orgId,
                                                        @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                        @RequestParam(value = "province", required = false) String province,
                                                        @RequestParam(value = "city", required = false) String city,
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

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        Page<EMRUserEntity> entityPage = emrUserRepository.findEventUsersFilterByWX(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream()
                .map(this::toEMRUserObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/win", method = RequestMethod.GET)
    public List<EMRUserObject> findEventUsersFilterByWin(@RequestParam(value = "org_id", required = false) String orgId,
                                                         @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                         @RequestParam(value = "province", required = false) String province,
                                                         @RequestParam(value = "city", required = false) String city,
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

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        Page<EMRUserEntity> entityPage = emrUserRepository.findEventUsersFilterByWin(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream()
                .map(this::toEMRUserObject)
                .collect(Collectors.toList());
    }


    @RequestMapping(value = "/reward", method = RequestMethod.GET)
    public List<EMRUserObject> findEventUsersFilterByReward(@RequestParam(value = "org_id", required = false) String orgId,
                                                            @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                            @RequestParam(value = "province", required = false) String province,
                                                            @RequestParam(value = "city", required = false) String city,
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

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        Page<EMRUserEntity> entityPage = emrUserRepository.findEventUsersFilterByReward(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream()
                .map(this::toEMRUserObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/share", method = RequestMethod.GET)
    public List<EMRUserObject> findEventUsersFilterByShare(@RequestParam(value = "org_id", required = false) String orgId,
                                                           @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                           @RequestParam(value = "province", required = false) String province,
                                                           @RequestParam(value = "city", required = false) String city,
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

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        Page<EMRUserEntity> entityPage = emrUserRepository.findEventUsersFilterByShare(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream()
                .map(this::toEMRUserObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/store_url", method = RequestMethod.GET)
    public List<EMRUserObject> findEventUsersFilterByStoreUrl(@RequestParam(value = "org_id", required = false) String orgId,
                                                              @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                              @RequestParam(value = "province", required = false) String province,
                                                              @RequestParam(value = "city", required = false) String city,
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

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        Page<EMRUserEntity> entityPage = emrUserRepository.findEventUsersFilterByStoreUrl(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream()
                .map(this::toEMRUserObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/comment", method = RequestMethod.GET)
    public List<EMRUserObject> findEventUsersFilterByComment(@RequestParam(value = "org_id", required = false) String orgId,
                                                             @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                             @RequestParam(value = "province", required = false) String province,
                                                             @RequestParam(value = "city", required = false) String city,
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

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        Page<EMRUserEntity> entityPage = emrUserRepository.findEventUsersFilterByComment(orgId, productBaseId, province, city, createdDateTimeStartTo, createdDateTimeEndTo, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream()
                .map(this::toEMRUserObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/event_stats", method = RequestMethod.GET)
    public List<EMRUserProductEventStasticsObject> queryUserEventStats(@RequestParam(value = "org_id", required = true) String orgId,
                                                                       @RequestParam(value = "ys_id", required = false) String ysId,
                                                                       @RequestParam(value = "user_id", required = false) String userId,
                                                                       @RequestParam(value = "create_datetime_start", required = false)
                                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                                                       @RequestParam(value = "create_datetime_end", required = false)
                                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                                                       HttpServletResponse response) {

        DateTime createdDateTimeStartTo = null;
        DateTime createdDateTimeEndTo = null;

        if (createdDateTimeStart != null && !StringUtils.isEmpty(createdDateTimeStart.toString()))
            createdDateTimeStartTo = createdDateTimeStart.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));

        if (createdDateTimeEnd != null && !StringUtils.isEmpty(createdDateTimeEnd.toString()))
            createdDateTimeEndTo = createdDateTimeEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);

        List<EMRUserProductEventStatistics> result = emrUserRepository.queryUserEventStatistics(orgId, userId, ysId, createdDateTimeStartTo, createdDateTimeEndTo);

        return result.stream()
                .map(this::toEMRUserProductEventStasticsObject)
                .collect(Collectors.toList());
    }


    private EMRUserProductEventStasticsObject toEMRUserProductEventStasticsObject(EMRUserProductEventStatistics entity) {
        if (entity == null) return null;
        EMRUserProductEventStasticsObject object = new EMRUserProductEventStasticsObject();
        object.setDrawCount(entity.getDrawCount());
        object.setOrgId(entity.getOrgId());
        object.setProductBaseId(entity.getProductBaseId());
        object.setProductName(entity.getProductName());
        object.setRewardCount(entity.getRewardCount());
        object.setScanCount(entity.getScanCount());
        object.setWinCount(entity.getWinCount());
        object.setCommentCount(entity.getCommentCount());
        object.setShareCount(entity.getShareCount());
        object.setStoreCount(entity.getStoreCount());
        //object.setWxScanCount(entity.getWxScanCount());

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
