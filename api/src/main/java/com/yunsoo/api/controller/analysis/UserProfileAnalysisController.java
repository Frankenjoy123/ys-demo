package com.yunsoo.api.controller.analysis;

import com.yunsoo.api.di.service.MarketUserAnalysisService;
import com.yunsoo.api.di.service.UserEventAnalysisService;
import com.yunsoo.api.di.service.UserProfileAnalysisService;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Xiaowu on 2016/11/21.
 */
@RestController
@RequestMapping("/bigdata2")
public class UserProfileAnalysisController {
    @Autowired
    private UserProfileAnalysisService userProfileAnalysisService;

    @Autowired
    private MarketUserAnalysisService marketUserAnalysisService;

    // region 用户属性分析

    @RequestMapping(value = "/user_profile_analysis", method = RequestMethod.GET)
    public MarketUserAnalysisReport getUserAnalysisReport(@RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                          @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime
    ) {

        LocalDate now = LocalDate.now();
        if (startTime == null) {
            startTime = now.plusDays(-90);
        }
        if (endTime == null) {
            endTime = now.plusDays(-1);
        }
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        MarketUserAnalysisReport report = new MarketUserAnalysisReport();
        MarketUserLocationReport locationReport = getUserLocationReport(orgId);
        MarketUserCategoryAndValueReport genderReport = getUserGenderReport(orgId);
        MarketUserCategoryAndValueReport deviceReport = getUserDeviceReport(orgId);
        MarketUserCategoryAndValueReport usageReport = getUserScanUsageReport(orgId, startTime, endTime);
        MarketUserCategoryAndValueReport areaReport = getUserAreaReport(orgId);


        report.setLocationReport(locationReport);
        report.setGenderReport(genderReport);
        report.setDeviceReport(deviceReport);
        report.setUsageReport(usageReport);
        report.setAreaReport(areaReport);

        return report;
    }

    private MarketUserCategoryAndValueReport getUserAreaReport(String orgId) {
        List<UserProfileTagCountObject> list = userProfileAnalysisService.getUserProfileAreaReport(orgId);
        Map<String, Integer> quantityMap = list.stream().collect(Collectors.toMap(UserProfileTagCountObject::getTag, t->t.getCount()));

        List<LuTagObject> tags = marketUserAnalysisService.getTags();
        List<String> categories = tags.stream().filter(t -> t.getCategory() == 1).map(LuTagObject::getTagName).collect(Collectors.toList());
        List<Integer> values = categories.stream().map(t -> {
            if (quantityMap.containsKey(t)) {
                return quantityMap.get(t);
            } else {
                return 0;
            }
        }).collect(Collectors.toList());

        MarketUserCategoryAndValueReport report = new MarketUserCategoryAndValueReport();
        report.setDataCategory(categories.toArray(new String[0]));
        report.setQuantity(values.stream().mapToInt(Integer::intValue).toArray());
        return report;
    }

    private MarketUserCategoryAndValueReport getUserScanUsageReport(String orgId, LocalDate startTime, LocalDate endTime) {
        List<UserProfileTagCountObject> list = userProfileAnalysisService.getUserProfileScanUsageReport(orgId, startTime, endTime);
        Map<String, Integer> quantityMap = list.stream().collect(Collectors.toMap(UserProfileTagCountObject::getTag, t->t.getCount()));

        List<LuTagObject> tags = marketUserAnalysisService.getTags();
        List<String> categories = tags.stream().filter(t -> t.getCategory() == 4).map(LuTagObject::getTagName).collect(Collectors.toList());
        List<Integer> values = tags.stream().filter(t -> t.getCategory() == 4).map(t -> {
            if (quantityMap.containsKey(t.getTagName())) {
                return quantityMap.get(t.getTagName());
            } else {
                return 0;
            }
        }).collect(Collectors.toList());
        MarketUserCategoryAndValueReport report = new MarketUserCategoryAndValueReport();
        report.setDataCategory(categories.toArray(new String[0]));
        report.setQuantity(values.stream().mapToInt(Integer::intValue).toArray());

        return report;
    }

    private MarketUserCategoryAndValueReport getUserDeviceReport(String orgId) {

        List<UserProfileTagCountObject> list = userProfileAnalysisService.getUserProfileDeviceReport(orgId);
        Map<String, Integer> quantityMap = list.stream().collect(Collectors.toMap(UserProfileTagCountObject::getTag, t->t.getCount()));

        List<LuTagObject> tags = marketUserAnalysisService.getTags();
        List<String> categories = tags.stream().filter(t -> t.getCategory() == 3).map(LuTagObject::getTagName).collect(Collectors.toList());
        List<Integer> values = tags.stream().filter(t -> t.getCategory() == 3).map(t -> {
            if (quantityMap.containsKey(t.getTagName())) {
                return quantityMap.get(t.getTagName());
            } else {
                return 0;
            }
        }).collect(Collectors.toList());
        MarketUserCategoryAndValueReport report = new MarketUserCategoryAndValueReport();
        report.setDataCategory(categories.toArray(new String[0]));
        report.setQuantity(values.stream().mapToInt(Integer::intValue).toArray());
        return report;
    }

    private MarketUserCategoryAndValueReport getUserGenderReport(String orgId) {
        List<UserProfileTagCountObject> list = userProfileAnalysisService.getUserProfileGenderReport(orgId);
        Map<String, Integer> quantityMap = list.stream().collect(Collectors.toMap(UserProfileTagCountObject::getTag, t->t.getCount()));
        List<LuTagObject> tags = marketUserAnalysisService.getTags();
        List<String> categories = tags.stream().filter(t -> t.getCategory() == 2).map(LuTagObject::getTagName).collect(Collectors.toList());
        List<Integer> values = tags.stream().filter(t -> t.getCategory() == 2).map(t -> {
            if (quantityMap.containsKey(t.getTagName())) {
                return quantityMap.get(t.getTagName());
            } else return 0;
        }).collect(Collectors.toList());
        MarketUserCategoryAndValueReport report = new MarketUserCategoryAndValueReport();
        report.setDataCategory(categories.toArray(new String[0]));
        report.setQuantity(values.stream().mapToInt(Integer::intValue).toArray());

        return report;
    }

    private MarketUserLocationReport getUserLocationReport(String orgId) {
        List<UserProfileLocationCountObject> list = userProfileAnalysisService.getUserProfileLocationReport(orgId);
        Map<String, Integer> quantityMap = list.stream()
                .collect(Collectors.groupingBy(UserProfileLocationCountObject::getProvince, Collectors.summingInt(UserProfileLocationCountObject::getCount)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException();
                }, LinkedHashMap::new));


        MarketUserLocationReport report = new MarketUserLocationReport();
        MarketUserLocationReport.NameValue[] data = quantityMap.entrySet().stream().map(entry -> {

            MarketUserLocationReport.NameValue nv = new MarketUserLocationReport.NameValue();
            nv.setName(entry.getKey());
            nv.setCount(entry.getValue());
            return nv;
        }).toArray(MarketUserLocationReport.NameValue[]::new);
        report.setProvinceData(data);
        return report;
    }

    // endregion
}
