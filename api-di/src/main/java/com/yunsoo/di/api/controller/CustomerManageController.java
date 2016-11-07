package com.yunsoo.di.api.controller;

import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.di.dao.entity.UserEntity;
import com.yunsoo.di.dao.repository.CustomerEventRepository;
import com.yunsoo.di.dao.repository.UserRepository;
import com.yunsoo.di.dto.*;
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
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private UserRepository userRepository;

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



    // query user daily user count and event count: scan , share, store_url, comment
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
                    objectMap.put(temp[0], countObject);
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
                        objectMap.put(tempShare[0], shareObject);
                    } else {
                        countShareObject.setShareEventCount(Integer.valueOf(tempShare[1]));
                        countShareObject.setShareUserCount(Integer.valueOf(tempShare[2]));
                        objectMap.put(tempShare[0], countShareObject);
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
                        objectMap.put(tempStoreUrl[0], storeUrlObject);
                    } else {
                        countStoreUrlObject.setStoreUrlEventCount(Integer.valueOf(tempStoreUrl[1]));
                        countStoreUrlObject.setStoreUrlUserCount(Integer.valueOf(tempStoreUrl[2]));
                        objectMap.put(tempStoreUrl[0], countStoreUrlObject);
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
                        objectMap.put(tempComment[0], commentObject);
                    } else {
                        countCommentObject.setCommentEventCount(Integer.valueOf(tempComment[1]));
                        countCommentObject.setCommentUserCount(Integer.valueOf(tempComment[2]));
                        objectMap.put(tempComment[0], countCommentObject);
                    }
                }
            }
        }
        emrEventReportObjectMap.setEvent_count(objectMap);
        return emrEventReportObjectMap;
    }

    // query user  event location count: scan, share, store_url, comment
    @RequestMapping(value = "/event/location", method = RequestMethod.GET)
    public List<EMREventLocationReportObject> queryUserEventLocationCount(@RequestParam(value = "org_id") String orgId,
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

        List<EMREventLocationReportObject> emrEventLocationReportObjectList = new ArrayList<>();

        // region ########加载all的数据，用来避免因为有用户同时跨省，跨市导致的数据不一致的问题
        List<int[]> bypassData = customerEventRepository.eventLocationCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        EMREventLocationReportObject bypassLocationObject = new EMREventLocationReportObject();
        bypassLocationObject.setProductBaseId(productBaseId);
        bypassLocationObject.setProvince(EMREventLocationReportObject.ALL_PROVINCE);
        bypassLocationObject.setCity(EMREventLocationReportObject.ALL_CITY);
        EMREventCountObject bypassEventCountObject = new EMREventCountObject();
        bypassEventCountObject.setScanEventCount(bypassData.get(0)[0]);
        bypassEventCountObject.setScanUserCount(bypassData.get(0)[1]);
        bypassEventCountObject.setShareEventCount(bypassData.get(1)[0]);
        bypassEventCountObject.setShareUserCount(bypassData.get(1)[1]);
        bypassEventCountObject.setStoreUrlEventCount(bypassData.get(2)[0]);
        bypassEventCountObject.setStoreUrlUserCount(bypassData.get(2)[1]);
        bypassEventCountObject.setCommentEventCount(bypassData.get(3)[0]);
        bypassEventCountObject.setCommentUserCount(bypassData.get(3)[1]);
        bypassLocationObject.setEvent_count(bypassEventCountObject);
        emrEventLocationReportObjectList.add(bypassLocationObject);
        // endregion

        List<String[]> scanList = customerEventRepository.scanLocationCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        if ((scanList != null) && (scanList.size() > 0)) {
            for (String[] temp : scanList) {
                EMREventLocationReportObject emrEventLocationReportObject = new EMREventLocationReportObject();
                EMREventCountObject emrEventCountObject = new EMREventCountObject();
                emrEventLocationReportObject.setProvince(temp[0]);
                emrEventLocationReportObject.setCity(temp[1]);
                emrEventLocationReportObject.setProductBaseId(productBaseId);

                emrEventCountObject.setScanEventCount(Integer.valueOf(temp[2]));
                emrEventCountObject.setScanUserCount(Integer.valueOf(temp[3]));
                emrEventLocationReportObject.setEvent_count(emrEventCountObject);
                emrEventLocationReportObjectList.add(emrEventLocationReportObject);
            }
        }

        List<String[]> shareList = customerEventRepository.shareLocationCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        if ((shareList != null) && (shareList.size() > 0)) {
            for (String[] tempShare : shareList) {
                EMREventLocationReportObject emrEventLocationReportObject = new EMREventLocationReportObject();
                EMREventCountObject emrEventCountObject = new EMREventCountObject();
                emrEventLocationReportObject.setProvince(tempShare[0]);
                emrEventLocationReportObject.setCity(tempShare[1]);
                emrEventLocationReportObject.setProductBaseId(productBaseId);

                emrEventCountObject.setShareEventCount(Integer.valueOf(tempShare[2]));
                emrEventCountObject.setShareUserCount(Integer.valueOf(tempShare[3]));
                emrEventLocationReportObject.setEvent_count(emrEventCountObject);
                emrEventLocationReportObjectList.add(emrEventLocationReportObject);
            }
        }

        List<String[]> storeUrlList = customerEventRepository.storeUrlLocationCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        if ((storeUrlList != null) && (storeUrlList.size() > 0)) {
            for (String[] tempStoreUrl : storeUrlList) {
                EMREventLocationReportObject emrEventLocationReportObject = new EMREventLocationReportObject();
                EMREventCountObject emrEventCountObject = new EMREventCountObject();
                emrEventLocationReportObject.setProvince(tempStoreUrl[0]);
                emrEventLocationReportObject.setCity(tempStoreUrl[1]);
                emrEventLocationReportObject.setProductBaseId(productBaseId);

                emrEventCountObject.setStoreUrlEventCount(Integer.valueOf(tempStoreUrl[2]));
                emrEventCountObject.setStoreUrlUserCount(Integer.valueOf(tempStoreUrl[3]));
                emrEventLocationReportObject.setEvent_count(emrEventCountObject);
                emrEventLocationReportObjectList.add(emrEventLocationReportObject);
            }
        }

        List<String[]> commentList = customerEventRepository.commentLocationCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        if ((commentList != null) && (commentList.size() > 0)) {
            for (String[] tempComment : commentList) {
                EMREventLocationReportObject emrEventLocationReportObject = new EMREventLocationReportObject();
                EMREventCountObject emrEventCountObject = new EMREventCountObject();
                emrEventLocationReportObject.setProvince(tempComment[0]);
                emrEventLocationReportObject.setCity(tempComment[1]);
                emrEventLocationReportObject.setProductBaseId(productBaseId);

                emrEventCountObject.setCommentEventCount(Integer.valueOf(tempComment[2]));
                emrEventCountObject.setCommentUserCount(Integer.valueOf(tempComment[3]));
                emrEventLocationReportObject.setEvent_count(emrEventCountObject);
                emrEventLocationReportObjectList.add(emrEventLocationReportObject);
            }
        }

        return emrEventLocationReportObjectList;
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

        Page<UserEntity> entityPage = userRepository.findByFilter(orgId, sex, phone, name ,province,city,ageStart, ageEnd, createdDateTimeStartTo, createdDateTimeEndTo,
                tags == null || tags.size() == 0 ? null : tags,
                tags == null || tags.size() == 0, wxUser, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }

        return entityPage.getContent().stream()
                .map(this::toEMRUserObject)
                .collect(Collectors.toList());
    }

    private EMRUserObject toEMRUserObject(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        EMRUserObject object = new EMRUserObject();
        object.setId(entity.getId());
        object.setUserId(entity.getUserId());
        object.setYsId(entity.getYsId());
        object.setOrgId(entity.getOrgId());
        object.setName(entity.getName());
        if (entity.getLuProvinceCityEntity()!=null){
            object.setProvince(entity.getLuProvinceCityEntity().getProvince());
            object.setCity(entity.getLuProvinceCityEntity().getCity());
        }
        object.setPhone(entity.getPhone());
        object.setWxOpenid(entity.getWxOpenId());
        object.setAge(entity.getAge());
        object.setSex(entity.getSex());
        object.setEmail(entity.getEmail());
        object.setGravatarUrl(entity.getGravatarUrl());
        object.setJoinDateTime(entity.getJoinDateTime());
        return object;
    }

}
