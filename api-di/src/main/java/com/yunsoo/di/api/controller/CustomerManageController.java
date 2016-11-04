package com.yunsoo.di.api.controller;

import com.yunsoo.di.dao.repository.CustomerEventRepository;
import com.yunsoo.di.dto.EMREventCountObject;
import com.yunsoo.di.dto.EMREventReportObject;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



    // query user daily user count and event count: share, store_url, comment
    @RequestMapping(value = "/event", method = RequestMethod.GET)
    public EMREventReportObject queryUserEventCount(@RequestParam(value = "org_id") String orgId,
                                                    @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                    @RequestParam(value = "province", required = false) String province,
                                                    @RequestParam(value = "city", required = false) String city,
                                                    @RequestParam(value = "create_datetime_start", required = false)
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                                    @RequestParam(value = "create_datetime_end", required = false)
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd) {

        DateTime startDateTime = null;
        DateTime endDateTime = null;

        if (createdDateTimeStart != null && !StringUtils.isEmpty(createdDateTimeStart.toString()))
            startDateTime = createdDateTimeStart.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));

        if (createdDateTimeEnd != null && !StringUtils.isEmpty(createdDateTimeEnd.toString()))
            endDateTime = createdDateTimeEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);

        productBaseId = StringUtils.isEmpty(productBaseId) ? null : productBaseId;

        EMREventReportObject emrEventReportObjectMap = new EMREventReportObject();
        Map<String, EMREventCountObject> objectMap = new HashMap<>();

        List<String[]> scanList = customerEventRepository.scanDailyCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        if ((scanList != null) && (scanList.size() > 0)) {
            for (String[] temp : scanList) {
                if (temp[0] != null && !temp[0].equals("")) {
                    EMREventCountObject countObject = new EMREventCountObject();
                    countObject.setScanEventCount(Integer.valueOf(temp[1]));
                    countObject.setScanUserCount(Integer.valueOf(temp[2]));
                    objectMap.put(temp[0].toString(), countObject);
                }
            }
        }

        List<String[]> shareList = customerEventRepository.shareDailyCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        if ((shareList != null) && (shareList.size() > 0)) {
            for (String[] tempShare : shareList) {
                if (tempShare[0] != null && !tempShare[0].equals("")) {
                    EMREventCountObject countShareObject = new EMREventCountObject();
                    EMREventCountObject shareObject = objectMap.get(tempShare[0]);
                    if (shareObject != null) {
                        shareObject.setShareEventCount(Integer.valueOf(tempShare[1]));
                        shareObject.setShareUserCount(Integer.valueOf(tempShare[2]));
                        objectMap.put(tempShare[0].toString(), shareObject);
                    } else {
                        countShareObject.setShareEventCount(Integer.valueOf(tempShare[1]));
                        countShareObject.setShareUserCount(Integer.valueOf(tempShare[2]));
                        objectMap.put(tempShare[0].toString(), countShareObject);
                    }
                }
            }
        }

        List<String[]> storeUrlList = customerEventRepository.storeUrlDailyCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        if ((storeUrlList != null) && (storeUrlList.size() > 0)) {
            for (String[] tempStoreUrl : storeUrlList) {
                if (tempStoreUrl[0] != null && !tempStoreUrl[0].equals("")) {
                    EMREventCountObject countStoreUrlObject = new EMREventCountObject();
                    EMREventCountObject storeUrlObject = objectMap.get(tempStoreUrl[0]);
                    if (storeUrlObject != null) {
                        storeUrlObject.setStoreUrlEventCount(Integer.valueOf(tempStoreUrl[1]));
                        storeUrlObject.setStoreUrlUserCount(Integer.valueOf(tempStoreUrl[2]));
                        objectMap.put(tempStoreUrl[0].toString(), storeUrlObject);
                    } else {
                        countStoreUrlObject.setStoreUrlEventCount(Integer.valueOf(tempStoreUrl[1]));
                        countStoreUrlObject.setStoreUrlUserCount(Integer.valueOf(tempStoreUrl[2]));
                        objectMap.put(tempStoreUrl[0].toString(), countStoreUrlObject);
                    }
                }
            }
        }

        List<String[]> commentList = customerEventRepository.commentDailyCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        if ((commentList != null) && (commentList.size() > 0)) {
            for (String[] tempComment : commentList) {
                if (tempComment[0] != null && !tempComment[0].equals("")) {
                    EMREventCountObject countCommentObject = new EMREventCountObject();
                    EMREventCountObject commentObject = objectMap.get(tempComment[0]);
                    if (commentObject != null) {
                        commentObject.setCommentEventCount(Integer.valueOf(tempComment[1]));
                        commentObject.setCommentUserCount(Integer.valueOf(tempComment[2]));
                        objectMap.put(tempComment[0].toString(), commentObject);
                    } else {
                        countCommentObject.setCommentEventCount(Integer.valueOf(tempComment[1]));
                        countCommentObject.setCommentUserCount(Integer.valueOf(tempComment[2]));
                        objectMap.put(tempComment[0].toString(), countCommentObject);
                    }
                }
            }
        }
        emrEventReportObjectMap.setEvent_count(objectMap);
        return emrEventReportObjectMap;
    }

}
