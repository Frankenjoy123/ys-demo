package com.yunsoo.api.controller.analysis;

import com.yunsoo.api.di.service.ScanAnalysisService;
import com.yunsoo.api.di.service.UserEventAnalysisService;
import com.yunsoo.api.dto.EMREventAnalysisReport;
import com.yunsoo.api.dto.EMREventLocationReport;
import com.yunsoo.api.dto.ScanAnalysisReport;
import com.yunsoo.api.dto.ScanLocationAnalysisReport;
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
 * Created by Xiaowu on 2016/11/18.
 */
@RestController
@RequestMapping("/bigdata")
public class EventAnalysisController {
    @Autowired
    private UserEventAnalysisService userEventAnalysisService;

    //  用戶看板
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

        EMREventReportObject emrEventReportObject = userEventAnalysisService.getEMREventReport(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd);

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
        } while (!startDateTime.isAfter(endDateTime));

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

        List<EMREventLocationReportObject> dataList = userEventAnalysisService.getEMRLocationReport(orgId, productBaseId, province, city, createdDateTimeStart, createdDateTimeEnd);

        EMREventLocationReport emrEventLocationReport = new EMREventLocationReport();

        // 为了避免用户跨省跨市导致的问题
        EMREventLocationReportObject allObject = dataList.stream().filter(t -> t.getProvince().equals(EMREventLocationReportObject.ALL_PROVINCE)).findFirst().get();
        EMREventLocationReport.NameValue allNV = new EMREventLocationReport.NameValue();
        allNV.setName(allObject.getProvince());
        EMREventCountObject eventCountObject = allObject.getEvent_count();
        allNV.setCommentEventCount(eventCountObject.getCommentEventCount());
        allNV.setCommentUserCount(eventCountObject.getCommentUserCount());
        allNV.setStoreUrlEventCount(eventCountObject.getStoreUrlEventCount());
        allNV.setStoreUrlUserCount(eventCountObject.getStoreUrlUserCount());
        allNV.setShareEventCount(eventCountObject.getShareEventCount());
        allNV.setShareUserCount(eventCountObject.getShareUserCount());
        allNV.setScanEventCount(eventCountObject.getScanEventCount());
        allNV.setScanUserCount(eventCountObject.getScanUserCount());
        emrEventLocationReport.setAggregatedData(allNV);

        List<EMREventLocationReportObject> locationList = dataList.stream().filter(t->!t.getProvince().equals(EMREventLocationReportObject.ALL_PROVINCE)).collect(Collectors.toList());
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
    // endregion

}
