package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AnalysisDomain;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Dake Wang on 2016/2/15.
 */
@RestController
@RequestMapping("/bigdata")
public class AnalysisController {
    @Autowired
    private AnalysisDomain analysisDomain;


    @RequestMapping(value = "/scan_report", method = RequestMethod.GET)
    public ScanAnalysisReport getScanAnalysisReport(@RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                    @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                    @RequestParam(value = "product_base_id", required = false) String productBaseId, @RequestParam(value = "batch_id", required = false) String batchId
    ) {
        LocalDate now = LocalDate.now();
        if (startTime == null) {
            startTime = now.plusDays(-90);
        }
        if (endTime == null) {
            endTime = now.plusDays(-1);
        }
        String orgId = AuthUtils.getCurrentAccount().getOrgId();

        List<ScanRecordAnalysisObject> data = analysisDomain.getScanAnalysisReport(orgId, startTime, endTime, productBaseId, batchId);
        Map<DateTime, Integer> pvData = data.stream().collect(Collectors.groupingBy(ScanRecordAnalysisObject::getScanDate, Collectors.summingInt(ScanRecordAnalysisObject::getPv)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException();
                }, LinkedHashMap::new));
        Map<DateTime, Integer> uvData = data.stream().collect(Collectors.groupingBy(ScanRecordAnalysisObject::getScanDate, Collectors.summingInt(ScanRecordAnalysisObject::getUv)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException();
                }, LinkedHashMap::new));
        ;


        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(0));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(0)).plusMinutes(10);


        List<Integer> pvList = new ArrayList<>();
        List<Integer> uvList = new ArrayList<>();
        List<String> dateList = new ArrayList<String>();

        do {
            int pv = 0;
            int uv = 0;
            if (pvData.containsKey(startDateTime)) {
                pv = pvData.get(startDateTime);
                uv = uvData.get(startDateTime);
            }
            pvList.add(pv);
            uvList.add(uv);
            dateList.add(startDateTime.toString("yyyy-MM-dd"));
            startDateTime = startDateTime.plusDays(1);

        } while (startDateTime.isBefore(endDateTime));


        ScanAnalysisReport report = new ScanAnalysisReport();
        ScanAnalysisReport.PVUV pvuv = new ScanAnalysisReport.PVUV();

        pvuv.setPv(pvList.stream().mapToInt(Integer::intValue).toArray());
        pvuv.setUv(uvList.stream().mapToInt(Integer::intValue).toArray());

        report.setData(pvuv);
        report.setDate(dateList.toArray(new String[0]));

        return report;

    }

    @RequestMapping(value = "/scan_location_report", method = RequestMethod.GET)
    public ScanLocationAnalysisReport getScanLocationAnalysisReport(@RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                    @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                    @RequestParam(value = "product_base_id", required = false) String productBaseId, @RequestParam(value = "batch_id", required = false) String batchId
    ) {
        LocalDate now = LocalDate.now();
        if (startTime == null) {
            startTime = now.plusDays(-90);
        }
        if (endTime == null) {
            endTime = now.plusDays(-1);
        }
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        List<ScanRecordLocationAnalysisObject> data = analysisDomain.getScanLocationAnalysisReport(orgId, startTime, endTime, productBaseId, batchId);

        ScanLocationAnalysisReport report = new ScanLocationAnalysisReport();
        Map<String, Integer> provinceData = data.stream().collect(Collectors.groupingBy(ScanRecordLocationAnalysisObject::getProvince, Collectors.summingInt(ScanRecordLocationAnalysisObject::getPv)));
        Map<String, Integer> provinceUvData = data.stream().collect(Collectors.groupingBy(ScanRecordLocationAnalysisObject::getProvince, Collectors.summingInt(ScanRecordLocationAnalysisObject::getUv)));

        int provincePVTotal = provinceData.values().stream().mapToInt(Integer::intValue).sum();
        int provinceUVTotal = provinceUvData.values().stream().mapToInt(Integer::intValue).sum();

        ScanLocationAnalysisReport.NameValue[] nvProvinceData = provinceData.entrySet().stream().map(e -> {
            ScanLocationAnalysisReport.NameValue nv = new ScanLocationAnalysisReport.NameValue();
            nv.setName(e.getKey());
            nv.setValue(e.getValue());
            double pvp = Math.round(e.getValue() * 10000.0 / provincePVTotal) / 100.0;
            nv.setPvPercentage(pvp);

            nv.setUv(provinceUvData.get(e.getKey()));
            double uvp = Math.round(nv.getUv() * 10000.0 / provinceUVTotal) / 100.0;
            nv.setUvPercentage(uvp);

            Map<String, Integer> cityData = data.stream().filter(p -> p.getProvince().equals(e.getKey())).collect(Collectors.groupingBy(ScanRecordLocationAnalysisObject::getCity, Collectors.summingInt(ScanRecordLocationAnalysisObject::getPv)));
            Map<String, Integer> cityUVData = data.stream().filter(p -> p.getProvince().equals(e.getKey())).collect(Collectors.groupingBy(ScanRecordLocationAnalysisObject::getCity, Collectors.summingInt(ScanRecordLocationAnalysisObject::getUv)));

            int cityPVTotal = cityData.values().stream().mapToInt(Integer::intValue).sum();
            int cityUVTotal = cityUVData.values().stream().mapToInt(Integer::intValue).sum();

            ScanLocationAnalysisReport.NameValue[] nvCityData = cityData.entrySet().stream().map(c -> {
                ScanLocationAnalysisReport.NameValue city = new ScanLocationAnalysisReport.NameValue();
                city.setName(c.getKey());
                city.setValue(c.getValue());

                double cityPvp = Math.round(c.getValue() * 10000.0 / cityPVTotal) / 100.0;
                city.setPvPercentage(cityPvp);

                city.setUv(cityUVData.get(c.getKey()));
                double cityUvp = Math.round(city.getUv() * 10000.0 / cityUVTotal) / 100.0;
                city.setUvPercentage(cityUvp);
                return city;
            }).collect(Collectors.toList()).toArray(new ScanLocationAnalysisReport.NameValue[0]);

            if (nvCityData.length > 1) {
                double pvOthers = Arrays.asList(nvCityData).stream().skip(1).mapToDouble(ScanLocationAnalysisReport.NameValue::getPvPercentage)
                        .sum();
                nvCityData[0].setPvPercentage(100 - pvOthers);
                double uvOthers = Arrays.asList(nvCityData).stream().skip(1).mapToDouble(ScanLocationAnalysisReport.NameValue::getUvPercentage)
                        .sum();
                nvCityData[0].setUvPercentage(100 - uvOthers);
            }
            nv.setCityData(nvCityData);

            return nv;
        }).collect(Collectors.toList()).toArray(new ScanLocationAnalysisReport.NameValue[0]);

        //调整最后一条记录
        if (nvProvinceData.length > 1) {
            double pvOthers = Arrays.asList(nvProvinceData).stream().skip(1).mapToDouble(ScanLocationAnalysisReport.NameValue::getPvPercentage)
                    .sum();
            pvOthers = Math.round(pvOthers * 100) / 100.0;
            nvProvinceData[0].setPvPercentage(100 - pvOthers);
            double uvOthers = Arrays.asList(nvProvinceData).stream().skip(1).mapToDouble(ScanLocationAnalysisReport.NameValue::getUvPercentage)
                    .sum();
            uvOthers = Math.round(uvOthers * 100) / 100.0;
            nvProvinceData[0].setUvPercentage(100 - uvOthers);
        }


        report.setProvinceData(nvProvinceData);

        return report;
    }

    @RequestMapping(value = "/key_usage_report", method = RequestMethod.GET)
    public KeyUsageReport getKeyUsageReport(@RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                            @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                            @RequestParam(value = "product_base_id", required = false) String productBaseId) {
        LocalDate now = LocalDate.now();
        if (startTime == null) {
            startTime = now.plusDays(-90);
        }
        if (endTime == null) {
            endTime = now.plusDays(-1);
        }
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        List<ProductKeyBatchObject> list = analysisDomain.getDailyKeyUsageReport(orgId, productBaseId, startTime, endTime);
        Map<String, Integer> quantityMap = list.stream()
                .collect(Collectors.groupingBy(o -> o.getCreatedDateTime().toString("yyyy-MM-dd"), Collectors.summingInt(ProductKeyBatchObject::getQuantity)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException();
                }, LinkedHashMap::new));


        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(0));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(0)).plusMinutes(10);
        List<String> dateList = new ArrayList<String>();
        List<Integer> quantityList = new ArrayList<Integer>();

        do {
            String dateString = startDateTime.toString("yyyy-MM-dd");
            dateList.add(dateString);
            startDateTime = startDateTime.plusDays(1);
            int quantity = 0;
            if (quantityMap.containsKey(dateString)) {
                quantity = quantityMap.get(dateString);
            }
            quantityList.add(quantity);
        } while (startDateTime.isBefore(endDateTime));


        KeyUsageReport report = new KeyUsageReport();
        report.setDate(dateList.toArray(new String[0]));
        report.setQuantity(quantityList.stream().mapToInt(Integer::intValue).toArray());

        return report;
    }

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
        List<MarketUserAreaAnalysisObject> list = analysisDomain.getMarketUserAreaReport(orgId, marketing_id, startTime, endTime);
        Map<String, Integer> quantityMap = list.stream()
                .collect(Collectors.groupingBy(MarketUserAreaAnalysisObject::getTagName, Collectors.summingInt(MarketUserAreaAnalysisObject::getCount)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException();
                }, LinkedHashMap::new));
        List<LuTagObject> tags = analysisDomain.getTags();
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
        List<MarketUserGenderAnalysisObject> list = analysisDomain.getMarketUserGenderReport(orgId, marketing_id, startTime, endTime);
        Map<String, Integer> quantityMap = list.stream()
                .collect(Collectors.groupingBy(t -> {
                    switch (t.getGender()) {
                        case 1://女
                            return "女";
                        case 0:
                            return "男";
                        default:
                            return "未知";
                    }

                }, Collectors.summingInt(MarketUserGenderAnalysisObject::getCount)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException();
                }, LinkedHashMap::new));

        List<LuTagObject> tags = analysisDomain.getTags();
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
        List<MarketUserDeviceAnalysisObject> list = analysisDomain.getMarketUserDeviceReport(orgId, marketing_id, startTime, endTime);
        Map<String, Integer> quantityMap = list.stream()
                .collect(Collectors.groupingBy(u->{
                    if (u.getDevice().equalsIgnoreCase("other")) {
                        return "其他";
                    }
                    return u.getDevice();

                }, Collectors.summingInt(MarketUserDeviceAnalysisObject::getCount)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException();
                }, LinkedHashMap::new));

        List<LuTagObject> tags = analysisDomain.getTags();
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
        List<MarketUserUsageAnalysisObject> list = analysisDomain.getMarketUserUsageReport(orgId, marketing_id, startTime, endTime);
        Map<String, Integer> quantityMap = list.stream()
                .collect(Collectors.groupingBy(MarketUserUsageAnalysisObject::getTimeSpan, Collectors.summingInt(MarketUserUsageAnalysisObject::getCount)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException();
                }, LinkedHashMap::new));

//        MarketUserCategoryAndValueReport report = new MarketUserCategoryAndValueReport();
//        report.setDataCategory(quantityMap.keySet().toArray(new String[0]));
//        report.setQuantity(quantityMap.values().stream().mapToInt(Integer::intValue).toArray());
        List<LuTagObject> tags = analysisDomain.getTags();
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
        List<MarketUserLocationAnalysisObject> list = analysisDomain.getMarketUserLocationReport(orgId, marketing_id, startTime, endTime);
        Map<String, Integer> quantityMap = list.stream()
                .collect(Collectors.groupingBy(MarketUserLocationAnalysisObject::getProvince, Collectors.summingInt(MarketUserLocationAnalysisObject::getCount)))
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

    @RequestMapping(value = "/event_report", method = RequestMethod.GET)
    public EMREventAnalysisReport getScanAnalysisReport(@RequestParam(value = "org_id", required = false) String orgId,
                                                        @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                        @RequestParam(value = "province", required = false) String province,
                                                        @RequestParam(value = "city", required = false) String city,
                                                        @RequestParam(value = "create_datetime_start", required = false)
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                                        @RequestParam(value = "create_datetime_end", required = false)
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd) {
        orgId = AuthUtils.fixOrgId(orgId);

        LocalDate now = LocalDate.now();
        if (createdDateTimeStart == null) {
            createdDateTimeStart = now.plusDays(-90);
        }
        if (createdDateTimeEnd == null) {
            createdDateTimeEnd = now.plusDays(-1);
        }


        EMREventReportObject emrEventReportObject = analysisDomain.getEMREventReport(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd);

        List<Integer> scanEventCount = new ArrayList<>();
        List<Integer> scanUserCount = new ArrayList<>();
        List<Integer> shareEventCount = new ArrayList<>();
        List<Integer> shareUserCount = new ArrayList<>();
        List<Integer> storeUrlEventCount = new ArrayList<>();
        List<Integer> storeUrlUserCount = new ArrayList<>();
        List<Integer> commentEventCount = new ArrayList<>();
        List<Integer> commentUserCount = new ArrayList<>();

        List<String> dateList = new ArrayList<String>();

        DateTime startDateTime = createdDateTimeStart.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(0));
        DateTime endDateTime = createdDateTimeEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(0));

        do {
            int scanEventData = 0;
            int scanUserData = 0;
            int shareEventData = 0;
            int shareUserData = 0;
            int storeEventData = 0;
            int storeUserData = 0;
            int commentEventData = 0;
            int commentUserData = 0;

            if (emrEventReportObject.getEvent_count().containsKey(startDateTime.toString("yyyy-MM-dd"))) {
                EMREventCountObject tempObject = emrEventReportObject.getEvent_count().get(startDateTime.toString("yyyy-MM-dd"));
                scanEventData = tempObject.getScanEventCount();
                scanUserData = tempObject.getScanUserCount();
                shareEventData = tempObject.getShareEventCount();
                shareUserData = tempObject.getShareUserCount();
                storeEventData = tempObject.getStoreUrlEventCount();
                storeUserData = tempObject.getStoreUrlUserCount();
                commentEventData = tempObject.getCommentEventCount();
                commentUserData = tempObject.getCommentUserCount();
            }
            scanEventCount.add(scanEventData);
            scanUserCount.add(scanUserData);
            shareEventCount.add(shareEventData);
            shareUserCount.add(shareUserData);
            storeUrlEventCount.add(storeEventData);
            storeUrlUserCount.add(storeUserData);
            commentEventCount.add(commentEventData);
            commentUserCount.add(commentUserData);

            dateList.add(startDateTime.toString("yyyy-MM-dd"));
            startDateTime = startDateTime.plusDays(1);
        } while (startDateTime.isBefore(endDateTime));

        EMREventAnalysisReport emrEventAnalysisReport = new EMREventAnalysisReport();

        EMREventAnalysisReport.EventCount eventCount = new EMREventAnalysisReport.EventCount();
        EMREventAnalysisReport.UserCount userCount = new EMREventAnalysisReport.UserCount();

        eventCount.setScanEventCount(scanEventCount.stream().mapToInt(Integer::intValue).toArray());
        eventCount.setShareEventCount(shareEventCount.stream().mapToInt(Integer::intValue).toArray());
        eventCount.setStoreUrlEventCount(storeUrlEventCount.stream().mapToInt(Integer::intValue).toArray());
        eventCount.setCommentEventCount(commentEventCount.stream().mapToInt(Integer::intValue).toArray());

        userCount.setScanUserCount(scanUserCount.stream().mapToInt(Integer::intValue).toArray());
        userCount.setShareUserCount(shareUserCount.stream().mapToInt(Integer::intValue).toArray());
        userCount.setStoreUrlUserCount(storeUrlUserCount.stream().mapToInt(Integer::intValue).toArray());
        userCount.setCommentUserCount(commentUserCount.stream().mapToInt(Integer::intValue).toArray());

        emrEventAnalysisReport.setEventCount(eventCount);
        emrEventAnalysisReport.setUserCount(userCount);
        emrEventAnalysisReport.setDate(dateList.toArray(new String[0]));

        return emrEventAnalysisReport;

    }

    @RequestMapping(value = "/event_location_report", method = RequestMethod.GET)
    public EMREventLocationReport getEventLocationReport(@RequestParam(value = "org_id", required = false) String orgId,
                                                         @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                         @RequestParam(value = "province", required = false) String province,
                                                         @RequestParam(value = "city", required = false) String city,
                                                         @RequestParam(value = "create_datetime_start", required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
                                                         @RequestParam(value = "create_datetime_end", required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd) {
        orgId = AuthUtils.fixOrgId(orgId);

        LocalDate now = LocalDate.now();
        if (createdDateTimeStart == null) {
            createdDateTimeStart = now.plusDays(-90);
        }
        if (createdDateTimeEnd == null) {
            createdDateTimeEnd = now.plusDays(-1);
        }

        List<EMREventLocationReportObject> locationList = analysisDomain.getEMRLocationReport(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd);

        EMREventLocationReport emrEventLocationReport = new EMREventLocationReport();

        // province Scan event data
        Map<String, Integer> provinceScanEventData = locationList.stream().collect(Collectors.groupingBy(EMREventLocationReportObject::getProvince, Collectors.summingInt(o -> {
            return o.getEvent_count().getScanEventCount();
        })));
        // province scan user data
        Map<String, Integer> provinceScanUserData = locationList.stream().collect(Collectors.groupingBy(EMREventLocationReportObject::getProvince, Collectors.summingInt(o -> {
            return o.getEvent_count().getScanUserCount();
        })));
        // province share event data
        Map<String, Integer> provinceShareEventData = locationList.stream().collect(Collectors.groupingBy(EMREventLocationReportObject::getProvince, Collectors.summingInt(o -> {
            return o.getEvent_count().getShareEventCount();
        })));
        // province share user data
        Map<String, Integer> provinceShareUserData = locationList.stream().collect(Collectors.groupingBy(EMREventLocationReportObject::getProvince, Collectors.summingInt(o -> {
            return o.getEvent_count().getShareUserCount();
        })));
        // province store_url event data
        Map<String, Integer> provinceStoreUrlEventData = locationList.stream().collect(Collectors.groupingBy(EMREventLocationReportObject::getProvince, Collectors.summingInt(o -> {
            return o.getEvent_count().getStoreUrlEventCount();
        })));
        // province store_url user data
        Map<String, Integer> provinceStoreUrlUserData = locationList.stream().collect(Collectors.groupingBy(EMREventLocationReportObject::getProvince, Collectors.summingInt(o -> {
            return o.getEvent_count().getStoreUrlUserCount();
        })));
        // province comment event data
        Map<String, Integer> provinceCommentEventData = locationList.stream().collect(Collectors.groupingBy(EMREventLocationReportObject::getProvince, Collectors.summingInt(o -> {
            return o.getEvent_count().getCommentEventCount();
        })));
        // province comment user data
        Map<String, Integer> provinceCommentUserData = locationList.stream().collect(Collectors.groupingBy(EMREventLocationReportObject::getProvince, Collectors.summingInt(o -> {
            return o.getEvent_count().getCommentUserCount();
        })));

        EMREventLocationReport.NameValue[] nvProvinceData = provinceScanEventData.entrySet().stream().map(e -> {
            EMREventLocationReport.NameValue nv = new EMREventLocationReport.NameValue();
            nv.setName(e.getKey());
            nv.setScanEventCount(e.getValue());
            if (provinceScanUserData.get(e.getKey()) != null) {
                nv.setScanUserCount(provinceScanUserData.get(e.getKey()));
            } else {
                nv.setScanUserCount(0);
            }
            if (provinceShareEventData.get(e.getKey()) != null) {
                nv.setShareEventCount(provinceShareEventData.get(e.getKey()));
            } else {
                nv.setShareEventCount(0);
            }
            if (provinceShareUserData.get(e.getKey()) != null) {
                nv.setShareUserCount(provinceShareUserData.get(e.getKey()));
            } else {
                nv.setShareUserCount(0);
            }
            if (provinceStoreUrlEventData.get(e.getKey()) != null) {
                nv.setStoreUrlEventCount(provinceStoreUrlEventData.get(e.getKey()));
            } else {
                nv.setStoreUrlEventCount(0);
            }
            if (provinceStoreUrlUserData.get(e.getKey()) != null) {
                nv.setStoreUrlUserCount(provinceStoreUrlUserData.get(e.getKey()));
            } else {
                nv.setStoreUrlUserCount(0);
            }
            if (provinceCommentEventData.get(e.getKey()) != null) {
                nv.setCommentEventCount(provinceCommentEventData.get(e.getKey()));
            } else {
                nv.setCommentEventCount(0);
            }
            if (provinceCommentUserData.get(e.getKey()) != null) {
                nv.setCommentUserCount(provinceCommentUserData.get(e.getKey()));
            } else {
                nv.setCommentUserCount(0);
            }

            Map<String, Integer> cityScanEventData = locationList.stream().filter(p -> p.getProvince().equals(e.getKey())).collect(Collectors.groupingBy(EMREventLocationReportObject::getCity, Collectors.summingInt(o -> {
                return o.getEvent_count().getScanEventCount();
            })));
            Map<String, Integer> cityScanUserData = locationList.stream().filter(p -> p.getProvince().equals(e.getKey())).collect(Collectors.groupingBy(EMREventLocationReportObject::getCity, Collectors.summingInt(o -> {
                return o.getEvent_count().getScanUserCount();
            })));
            Map<String, Integer> cityShareEventData = locationList.stream().filter(p -> p.getProvince().equals(e.getKey())).collect(Collectors.groupingBy(EMREventLocationReportObject::getCity, Collectors.summingInt(o -> {
                return o.getEvent_count().getShareEventCount();
            })));
            Map<String, Integer> cityShareUserData = locationList.stream().filter(p -> p.getProvince().equals(e.getKey())).collect(Collectors.groupingBy(EMREventLocationReportObject::getCity, Collectors.summingInt(o -> {
                return o.getEvent_count().getShareUserCount();
            })));
            Map<String, Integer> cityStoreUrlEventData = locationList.stream().filter(p -> p.getProvince().equals(e.getKey())).collect(Collectors.groupingBy(EMREventLocationReportObject::getCity, Collectors.summingInt(o -> {
                return o.getEvent_count().getStoreUrlEventCount();
            })));
            Map<String, Integer> cityStoreUrlUserData = locationList.stream().filter(p -> p.getProvince().equals(e.getKey())).collect(Collectors.groupingBy(EMREventLocationReportObject::getCity, Collectors.summingInt(o -> {
                return o.getEvent_count().getStoreUrlUserCount();
            })));
            Map<String, Integer> cityCommentEventData = locationList.stream().filter(p -> p.getProvince().equals(e.getKey())).collect(Collectors.groupingBy(EMREventLocationReportObject::getCity, Collectors.summingInt(o -> {
                return o.getEvent_count().getCommentEventCount();
            })));
            Map<String, Integer> cityCommentUserData = locationList.stream().filter(p -> p.getProvince().equals(e.getKey())).collect(Collectors.groupingBy(EMREventLocationReportObject::getCity, Collectors.summingInt(o -> {
                return o.getEvent_count().getCommentUserCount();
            })));

            EMREventLocationReport.NameValue[] nvCityData = cityScanEventData.entrySet().stream().map(c -> {
                EMREventLocationReport.NameValue cityData = new EMREventLocationReport.NameValue();
                cityData.setName(c.getKey());
                cityData.setScanEventCount(c.getValue());
                if (cityScanUserData.get(c.getKey()) != null) {
                    cityData.setScanUserCount(cityScanUserData.get(c.getKey()));
                } else {
                    cityData.setScanUserCount(0);
                }
                if (cityShareEventData.get(c.getKey()) != null) {
                    cityData.setShareEventCount(cityShareEventData.get(c.getKey()));
                } else {
                    cityData.setShareEventCount(0);
                }
                if (cityShareUserData.get(c.getKey()) != null) {
                    cityData.setShareUserCount(cityShareUserData.get(c.getKey()));
                } else {
                    cityData.setShareUserCount(0);
                }
                if (cityStoreUrlEventData.get(c.getKey()) != null) {
                    cityData.setStoreUrlEventCount(cityStoreUrlEventData.get(c.getKey()));
                } else {
                    cityData.setStoreUrlEventCount(0);
                }
                if (cityStoreUrlUserData.get(c.getKey()) != null) {
                    cityData.setStoreUrlUserCount(cityStoreUrlUserData.get(c.getKey()));
                } else {
                    cityData.setStoreUrlUserCount(0);
                }
                if (cityCommentEventData.get(c.getKey()) != null) {
                    cityData.setCommentEventCount(cityCommentEventData.get(c.getKey()));
                } else {
                    cityData.setCommentEventCount(0);
                }
                if (cityCommentUserData.get(c.getKey()) != null) {
                    cityData.setCommentUserCount(cityCommentUserData.get(c.getKey()));
                } else {
                    cityData.setCommentUserCount(0);
                }
                return cityData;
            }).collect(Collectors.toList()).toArray(new EMREventLocationReport.NameValue[0]);

            nv.setCityData(nvCityData);
            return nv;
        }).collect(Collectors.toList()).toArray(new EMREventLocationReport.NameValue[0]);

        emrEventLocationReport.setProvinceData(nvProvinceData);
        return emrEventLocationReport;
    }
}
