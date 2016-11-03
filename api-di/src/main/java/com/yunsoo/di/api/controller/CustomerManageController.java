package com.yunsoo.di.api.controller;

import com.yunsoo.di.dao.repository.CustomerEventRepository;
import com.yunsoo.di.dto.EMRUserReportObject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by:   xiaowu
 * Created on:   2016/11/3
 * Descriptions: customer management  api
 */
@RestController
@RequestMapping("/user")
public class CustomerManageController {

    @Autowired
    private CustomerEventRepository customerEventRepository;

    // event count and user count about scan, draw, win, reward
    @RequestMapping(value = "/funnel", method = RequestMethod.GET)
    public EMRUserReportObject queryUserFunnel(@RequestParam(value = "org_id") String orgId,
                                               @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                               @RequestParam(value = "province", required = false) String province,
                                               @RequestParam(value = "city", required = false) String city,
                                               @RequestParam(value = "create_datetime_start", required = false)
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                               @RequestParam(value = "create_datetime_end", required = false)
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
                                               @RequestParam(value = "scan_source", required = false) String scanSource) {

        DateTime startDateTime = null;
        DateTime endDateTime = null;

        if (createdDateTimeStart != null && !StringUtils.isEmpty(createdDateTimeStart.toString()))
            startDateTime = createdDateTimeStart.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));

        if (createdDateTimeEnd != null && !StringUtils.isEmpty(createdDateTimeEnd.toString()))
            endDateTime = createdDateTimeEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        List<Integer> eventCount = new ArrayList<>();
        List<Integer> userCount = new ArrayList<>();

        int[] scanData = customerEventRepository.scanCount(orgId, productBaseId, province, city, startDateTime, endDateTime,scanSource);
        eventCount.add(scanData[0]);
        userCount.add(scanData[1]);


        int[] drawCount = customerEventRepository.drawCount(orgId, productBaseId, province, city, startDateTime, endDateTime,scanSource);
        eventCount.add(drawCount[0]);
        userCount.add(drawCount[1]);

        int[] winCount = customerEventRepository.winCount(orgId, productBaseId, province, city, startDateTime, endDateTime,scanSource);
        eventCount.add(winCount[0]);
        userCount.add(winCount[1]);

        int[] rewardCount = customerEventRepository.rewardCount(orgId, productBaseId, province, city, startDateTime, endDateTime,scanSource);
        eventCount.add(rewardCount[0]);
        userCount.add(rewardCount[1]);

        EMRUserReportObject report = new EMRUserReportObject();
        report.setEventCount(eventCount);
        report.setUserCount(userCount);
        return report;
    }

}
