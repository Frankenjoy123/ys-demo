package com.yunsoo.di.api.controller;

import com.yunsoo.di.dao.entity.UserProfileTagCountEntity;
import com.yunsoo.di.dao.repository.UserProfileRepository;
import com.yunsoo.di.dto.UserProfileTagCountObject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   xiaowu
 * Created on:   2016/11/1
 * Descriptions: user profile api
 */

@RestController
@RequestMapping("/user_profile")
public class UserProfileController {

    // 用户属性分析
    @Autowired
    private UserProfileRepository userProfileRepository;


    @RequestMapping(value = "/area", method = RequestMethod.GET)
    public List<UserProfileTagCountObject> queryUserProfileArea(@RequestParam(value = "org_id") String orgId) {
        List<UserProfileTagCountEntity> list = userProfileRepository.queryUserProfileAreaReport(orgId);
        return list.stream().map(UserProfileTagCountEntity::toDataObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "/scan_time_range", method = RequestMethod.GET)
    public List<UserProfileTagCountObject> queryUserProfileTimeRange(@RequestParam(value = "org_id") String orgId,
                                                                     @RequestParam(value = "start_time")
                                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                     @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime

    ) {
        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<UserProfileTagCountEntity> list = userProfileRepository.queryUserProfileTimeUsage(orgId, startDateTime, endDateTime);
        return list.stream().map(UserProfileTagCountEntity::toDataObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "/device", method = RequestMethod.GET)
    public List<UserProfileTagCountObject> queryUserProfileDevice(@RequestParam(value = "org_id") String orgId) {

        List<UserProfileTagCountEntity> list = userProfileRepository.queryUserProfileDeviceUsage(orgId);
        return list.stream().map(UserProfileTagCountEntity::toDataObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "/gender", method = RequestMethod.GET)
    public List<UserProfileTagCountObject> queryUserProfileGender(@RequestParam(value = "org_id") String orgId) {
        List<UserProfileTagCountEntity> list = userProfileRepository.queryUserProfileGenderUsage(orgId);
        return list.stream().map(UserProfileTagCountEntity::toDataObject).collect(Collectors.toList());
    }

}
