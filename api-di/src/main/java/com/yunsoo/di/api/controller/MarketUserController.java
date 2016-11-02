package com.yunsoo.di.api.controller;

import com.yunsoo.di.dao.entity.LuTagEntity;
import com.yunsoo.di.dao.entity.UserProfileLocationCountEntity;
import com.yunsoo.di.dao.entity.UserProfileTagCountEntity;
import com.yunsoo.di.dao.repository.LuTagRepository;
import com.yunsoo.di.dao.repository.MarketUserRepository;
import com.yunsoo.di.dto.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   xiaowu
 * Created on:   2016/11/1
 * Descriptions: marketing user api
 */
@RestController
@RequestMapping("/market_user")
public class MarketUserController {

    @Autowired
    private MarketUserRepository marketUserRepository;

    @Autowired
    private LuTagRepository luTagRepository;

    // 一线、二线、三线、其他
    @RequestMapping(value = "/area", method = RequestMethod.GET)
    public List<UserProfileTagCountObject> queryMarketUserArea(@RequestParam(value = "org_id") String orgId,
                                                                  @RequestParam(value = "start_time")
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                  @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                  @RequestParam(value = "marketing_id", required = false) String marketingId
    ) {
        if (StringUtils.isEmpty(marketingId))
            marketingId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<UserProfileTagCountEntity> list = marketUserRepository.queryMarketUserAreaAnalysis(orgId, startDateTime, endDateTime, marketingId);
        return list.stream().map(UserProfileTagCountEntity::toDataObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "/device", method = RequestMethod.GET)
    public List<UserProfileTagCountObject> queryMarketUserDevice(@RequestParam(value = "org_id") String orgId,
                                                                      @RequestParam(value = "start_time")
                                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                      @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                      @RequestParam(value = "marketing_id", required = false) String marketingId
    ) {
        if (StringUtils.isEmpty(marketingId))
            marketingId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<UserProfileTagCountEntity> list = marketUserRepository.queryMarketUserDeviceAnalysis(orgId, startDateTime, endDateTime, marketingId);
        return list.stream().map(UserProfileTagCountEntity::toDataObject).collect(Collectors.toList());
    }

    // 性别分析 (0为男，1为女，2为未知)
    @RequestMapping(value = "/gender", method = RequestMethod.GET)
    public List<UserProfileTagCountObject> queryMarketUserGender(@RequestParam(value = "org_id") String orgId,
                                                                      @RequestParam(value = "start_time")
                                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                      @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                      @RequestParam(value = "marketing_id", required = false) String marketingId
    ) {
        if (StringUtils.isEmpty(marketingId))
            marketingId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<UserProfileTagCountEntity> list = marketUserRepository.queryMarketUserGenderAnalysis(orgId, startDateTime, endDateTime, marketingId);
        return list.stream().map(UserProfileTagCountEntity::toDataObject).collect(Collectors.toList());
    }

    // 时间段分析，使用习惯 0-6 6-8 8-12 12-14 14-16 16-18 18-22 22-24
    @RequestMapping(value = "/usage", method = RequestMethod.GET)
    public List<UserProfileTagCountObject> queryMarketUserUsage(@RequestParam(value = "org_id") String orgId,
                                                                    @RequestParam(value = "start_time")
                                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                    @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                    @RequestParam(value = "marketing_id", required = false) String marketingId
    ) {
        if (StringUtils.isEmpty(marketingId))
            marketingId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<UserProfileTagCountEntity> list = marketUserRepository.queryMarketUserUsageAnalysis(orgId, startDateTime, endDateTime, marketingId);
        return list.stream().map(UserProfileTagCountEntity::toDataObject).collect(Collectors.toList());
    }

    // 用户地域分析
    @RequestMapping(value = "/location", method = RequestMethod.GET)
    public List<UserProfileLocationCountObject> queryMarketUserLocaiton(@RequestParam(value = "org_id") String orgId,
                                                                          @RequestParam(value = "start_time")
                                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                          @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                          @RequestParam(value = "marketing_id", required = false) String marketingId
    ) {
        if (StringUtils.isEmpty(marketingId))
            marketingId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<UserProfileLocationCountEntity> list = marketUserRepository.queryMarketUserLocationAnalysis(orgId, startDateTime, endDateTime, marketingId);
        return list.stream().map(UserProfileLocationCountEntity::toDataObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "tags", method = RequestMethod.GET)
    public List<LuTagObject> queryMarketUserTag() {

        List<LuTagEntity> list = luTagRepository.findAll();
        return list.stream().map(LuTagEntity::toDataObject).collect(Collectors.toList());
    }

}
