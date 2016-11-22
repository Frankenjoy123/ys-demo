package com.yunsoo.api.controller.analysis;

import com.yunsoo.api.di.service.MarketUserAnalysisService;
import com.yunsoo.api.di.service.ScanAnalysisService;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Xiaowu on 2016/11/18.
 */
@RestController
@RequestMapping("/bigdata")
public class MarketUserAnalysisController {
    @Autowired
    private MarketUserAnalysisService marketUserAnalysisService;

    //  营销用户分析
    @RequestMapping(value = "/market_user_area_report", method = RequestMethod.GET)
    public MarketUserCategoryAndValueReport getMarketUserAreaReport(@RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                    @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                    @RequestParam(value = "marketing_id", required = false) String marketing_id) {
        LocalDate now = LocalDate.now();
        if (startTime == null) {
            startTime = now.plusDays(-90);
        }
        if (endTime == null) {
            endTime = now.plusDays(-1);
        }
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        List<UserProfileTagCountObject> list = marketUserAnalysisService.getMarketUserAreaReport(orgId, marketing_id, startTime, endTime);
        Map<String, Integer> quantityMap = list.stream()
                .collect(Collectors.groupingBy(UserProfileTagCountObject::getTag, Collectors.summingInt(UserProfileTagCountObject::getCount)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException();
                }, LinkedHashMap::new));
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

    @RequestMapping(value = "/market_user_gender_report", method = RequestMethod.GET)
    public MarketUserCategoryAndValueReport getMarketUserGenderReport(@RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                      @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                      @RequestParam(value = "marketing_id", required = false) String marketing_id) {
        LocalDate now = LocalDate.now();
        if (startTime == null) {
            startTime = now.plusDays(-90);
        }
        if (endTime == null) {
            endTime = now.plusDays(-1);
        }
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        List<UserProfileTagCountObject> list = marketUserAnalysisService.getMarketUserGenderReport(orgId, marketing_id, startTime, endTime);

        Map<String, Integer> quantityMap = list.stream()
                .collect(Collectors.groupingBy(UserProfileTagCountObject::getTag, Collectors.summingInt(UserProfileTagCountObject::getCount)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException();
                }, LinkedHashMap::new));


        List<LuTagObject> tags = marketUserAnalysisService.getTags();
        List<String> categories = tags.stream().filter(t -> t.getCategory() == 2).map(LuTagObject::getTagName).collect(Collectors.toList());
        List<Integer> values = tags.stream().filter(t -> t.getCategory() == 2).map(t -> {
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

    @RequestMapping(value = "/market_user_device_report", method = RequestMethod.GET)
    public MarketUserCategoryAndValueReport getMarketUserDeviceReport(@RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                      @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                      @RequestParam(value = "marketing_id", required = false) String marketing_id) {
        LocalDate now = LocalDate.now();
        if (startTime == null) {
            startTime = now.plusDays(-90);
        }
        if (endTime == null) {
            endTime = now.plusDays(-1);
        }
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        List<UserProfileTagCountObject> list = marketUserAnalysisService.getMarketUserDeviceReport(orgId, marketing_id, startTime, endTime);
        Map<String, Integer> quantityMap = list.stream()
                .collect(Collectors.groupingBy(u->{
                    if (u.getTag().equalsIgnoreCase("iOS")) {
                        return "iPhone";
                    }
                   else if (StringUtils.isEmpty(u.getTag())||u.getTag().equalsIgnoreCase("Unknown")||u.getTag().equalsIgnoreCase("Mac OS X")) {
                        return "其他";
                    }
                    return u.getTag();

                }, Collectors.summingInt(UserProfileTagCountObject::getCount)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException();
                }, LinkedHashMap::new));

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

    @RequestMapping(value = "/market_user_usage_report", method = RequestMethod.GET)
    public MarketUserCategoryAndValueReport getMarketUserUsageReport(@RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                     @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                     @RequestParam(value = "marketing_id", required = false) String marketing_id) {
        LocalDate now = LocalDate.now();
        if (startTime == null) {
            startTime = now.plusDays(-90);
        }
        if (endTime == null) {
            endTime = now.plusDays(-1);
        }
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        List<UserProfileTagCountObject> list = marketUserAnalysisService.getMarketUserUsageReport(orgId, marketing_id, startTime, endTime);
        Map<String, Integer> quantityMap = list.stream()
                .collect(Collectors.groupingBy(UserProfileTagCountObject::getTag, Collectors.summingInt(UserProfileTagCountObject::getCount)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException();
                }, LinkedHashMap::new));

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

    @RequestMapping(value = "/market_user_location_report", method = RequestMethod.GET)
    public MarketUserLocationReport getMarketUserLocationReport(@RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                @RequestParam(value = "marketing_id", required = false) String marketing_id) {
        LocalDate now = LocalDate.now();
        if (startTime == null) {
            startTime = now.plusDays(-90);
        }
        if (endTime == null) {
            endTime = now.plusDays(-1);
        }
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        List<UserProfileLocationCountObject> list = marketUserAnalysisService.getMarketUserLocationReport(orgId, marketing_id, startTime, endTime);
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


    /**
     * 用于整个数据分析的加载
     * @param startTime
     * @param endTime
     * @param marketing_id
     * @return
     */
    @RequestMapping(value = "/market_user_analysis", method = RequestMethod.GET)
    public MarketUserAnalysisReport getMarketUserAnalysisReport(@RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                @RequestParam(value = "marketing_id", required = false) String marketing_id) {
        MarketUserAnalysisReport report = new MarketUserAnalysisReport();

        MarketUserLocationReport locationReport = getMarketUserLocationReport(startTime, endTime, marketing_id);
        MarketUserCategoryAndValueReport genderReport = getMarketUserGenderReport(startTime, endTime, marketing_id);
        MarketUserCategoryAndValueReport deviceReport = getMarketUserDeviceReport(startTime, endTime, marketing_id);
        MarketUserCategoryAndValueReport usageReport = getMarketUserUsageReport(startTime, endTime, marketing_id);
        MarketUserCategoryAndValueReport areaReport = getMarketUserAreaReport(startTime, endTime, marketing_id);


        report.setLocationReport(locationReport);
        report.setGenderReport(genderReport);
        report.setDeviceReport(deviceReport);
        report.setUsageReport(usageReport);
        report.setAreaReport(areaReport);

        return report;
    }

}
