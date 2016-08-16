package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.*;
import com.yunsoo.data.service.entity.*;
import com.yunsoo.data.service.repository.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Dake Wang on 2016/2/4.
 */


@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Autowired
    private ScanRecordLocationAnalysisRepository scanRecordLocationAnalysisRepository;

    @Autowired
    private ScanRecordAnalysisRepository scanRecordAnalysisRepository;

    // S 新增营销方案的repository
    @Autowired
    private MarketUserAreaAnalysisRepository marketUserAreaAnalysisRepository;
    @Autowired
    private MarketUserDeviceAnalysisRepository marketUserDeviceAnalysisRepository;
    @Autowired
    private MarketUserGenderAnalysisRepository marketUserGenderAnalysisRepository;
    @Autowired
    private MarketUserUsageAnalysisRepository marketUserUsageAnalysisRepository;
    @Autowired
    private MarketUserLocationAnalysisRepository marketUserLocationAnalysisRepository;

    @Autowired
    private LuTagRepository luTagRepository;
    // E 营销方案

    //S 消费者漏斗分析
    @Autowired
    private EMREventRepository eventRepository;


    @Autowired
    private ProductKeyBatchRepository productKeyBatchRepository;


    @RequestMapping(value = "/scan_data", method = RequestMethod.GET)
    public List<ScanRecordAnalysisObject> query(@RequestParam(value = "org_id") String orgId,
                                                @RequestParam(value = "start_time")
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                @RequestParam(value = "batch_id", required = false) String batchId) {

        if (StringUtils.isEmpty(productBaseId)) {
            // 所有产品维度
            productBaseId = "All";
        }
        if (StringUtils.isEmpty(batchId))
            batchId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<ScanRecordAnalysisEntity> list = scanRecordAnalysisRepository.query(orgId, startDateTime, endDateTime, productBaseId, batchId);
        return list.stream().map(ScanRecordAnalysisEntity::toDataObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "/scan_data_location", method = RequestMethod.GET)
    public List<ScanRecordLocationAnalysisObject> queryWithLocation(@RequestParam(value = "org_id") String orgId,
                                                                    @RequestParam(value = "start_time")
                                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                    @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                    @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                                    @RequestParam(value = "batch_id", required = false) String batchId) {
        if (StringUtils.isEmpty(productBaseId))
            productBaseId = null;
        if (StringUtils.isEmpty(batchId))
            batchId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<ScanRecordLocationAnalysisEntity> list = eventRepository.consumerLocationCount(orgId, productBaseId,batchId, startDateTime, endDateTime);
        return list.stream().map(ScanRecordLocationAnalysisEntity::toDataObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "/batch_key_report", method = RequestMethod.GET)
    public List<ProductKeyBatchObject> queryBatchKeyReport(@RequestParam(value = "org_id") String orgId,
                                                           @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                           @RequestParam(value = "start_time")
                                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                           @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime

    ) {
        if (StringUtils.isEmpty(productBaseId))
            productBaseId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<ProductKeyBatchEntity> list = productKeyBatchRepository.queryDailyKeyUsageReport(orgId, productBaseId, startDateTime, endDateTime);
        return list.stream().map(ProductKeyBatchEntity::toDataObject).collect(Collectors.toList());
    }

    //TODO 新增营销报表，城市微群, tag_name 为重点
    @RequestMapping(value = "/market_user_area", method = RequestMethod.GET)
    public List<MarketUserAreaAnalysisObject> queryMarketUserArea(@RequestParam(value = "org_id") String orgId,
                                                                  @RequestParam(value = "start_time")
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                  @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                  @RequestParam(value = "marketing_id", required = false) String marketingId
    ) {
        if (StringUtils.isEmpty(marketingId))
            marketingId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<MarketUserAreaAnalysisEntity> list = marketUserAreaAnalysisRepository.query(orgId, startDateTime, endDateTime, marketingId);
        return list.stream().map(MarketUserAreaAnalysisEntity::toDataObject).collect(Collectors.toList());
    }

    // TODO 新增营销报表，设备分析 (other 可能要改为多语言，改成其他）
    @RequestMapping(value = "/market_user_device", method = RequestMethod.GET)
    public List<MarketUserDeviceAnalysisObject> queryMarketUserDevice(@RequestParam(value = "org_id") String orgId,
                                                                      @RequestParam(value = "start_time")
                                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                      @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                      @RequestParam(value = "marketing_id", required = false) String marketingId
    ) {
        if (StringUtils.isEmpty(marketingId))
            marketingId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<MarketUserDeviceAnalysisEntity> list = marketUserDeviceAnalysisRepository.query(orgId, startDateTime, endDateTime, marketingId);
        return list.stream().map(MarketUserDeviceAnalysisEntity::toDataObject).collect(Collectors.toList());
    }

    // TODO  新增营销报表，性别分析 (0为男，1为女，2为未知)
    @RequestMapping(value = "/market_user_gender", method = RequestMethod.GET)
    public List<MarketUserGenderAnalysisObject> queryMarketUserGender(@RequestParam(value = "org_id") String orgId,
                                                                      @RequestParam(value = "start_time")
                                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                      @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                      @RequestParam(value = "marketing_id", required = false) String marketingId
    ) {
        if (StringUtils.isEmpty(marketingId))
            marketingId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<MarketUserGenderAnalysisEntity> list = marketUserGenderAnalysisRepository.query(orgId, startDateTime, endDateTime, marketingId);
        return list.stream().map(MarketUserGenderAnalysisEntity::toDataObject).collect(Collectors.toList());
    }

    // TODO  新增营销报表，使用习惯 0-6 6-8 8-12 12-14 14-16 16-18 18-22 22-24
    @RequestMapping(value = "/market_user_usage", method = RequestMethod.GET)
    public List<MarketUserUsageAnalysisObject> queryMarketUserUsage(@RequestParam(value = "org_id") String orgId,
                                                                    @RequestParam(value = "start_time")
                                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                    @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                    @RequestParam(value = "marketing_id", required = false) String marketingId
    ) {
        if (StringUtils.isEmpty(marketingId))
            marketingId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<MarketUserUsageAnalysisEntity> list = marketUserUsageAnalysisRepository.query(orgId, startDateTime, endDateTime, marketingId);
        return list.stream().map(MarketUserUsageAnalysisEntity::toDataObject).collect(Collectors.toList());
    }


    // TODO  新增营销报表，用户地域分析
    @RequestMapping(value = "/market_user_location", method = RequestMethod.GET)
    public List<MarketUserLocationAnalysisObject> queryMarketUserLocaiton(@RequestParam(value = "org_id") String orgId,
                                                                          @RequestParam(value = "start_time")
                                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                          @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                          @RequestParam(value = "marketing_id", required = false) String marketingId
    ) {
        if (StringUtils.isEmpty(marketingId))
            marketingId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<MarketUserLocationAnalysisEntity> list = marketUserLocationAnalysisRepository.query(orgId, startDateTime, endDateTime, marketingId);
        return list.stream().map(MarketUserLocationAnalysisEntity::toDataObject).collect(Collectors.toList());
    }

    // TODO  新增营销报表，用户地域分析
    @RequestMapping(value = "/market_user_tags", method = RequestMethod.GET)
    public List<LuTagObject> queryMarketUserTag() {

        List<LuTagEntity> list = luTagRepository.findAll();
        return list.stream().map(LuTagEntity::toDataObject).collect(Collectors.toList());
    }

    // int[] 1222,222,22,2,0
    @RequestMapping(value = "/user/funnel", method = RequestMethod.GET)
    public EMRUserReportObject queryUserFunnel(@RequestParam(value = "org_id") String orgId,
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

        List<Integer> eventCount = new ArrayList<>();
        List<Integer> userCount = new ArrayList<>();
        int[] scanData = eventRepository.scanCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        eventCount.add(scanData[0]);
        userCount.add(scanData[1]);

        int[] wxCount = eventRepository.wxCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        eventCount.add(wxCount[0]);
        userCount.add(wxCount[1]);

        int[] drawCount = eventRepository.drawCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        eventCount.add(drawCount[0]);
        userCount.add(drawCount[1]);

        int[] winCount = eventRepository.winCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        eventCount.add(winCount[0]);
        userCount.add(winCount[1]);

        int[] rewardCount = eventRepository.rewardCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        eventCount.add(rewardCount[0]);
        userCount.add(rewardCount[1]);

        EMRUserReportObject report = new EMRUserReportObject();
        report.setEventCount(eventCount);
        report.setUserCount(userCount);
        return report;
    }

    // query user action count: share, store_url, comment
    @RequestMapping(value = "/user/action", method = RequestMethod.GET)
    public EMRActionReportObject queryUserAction(@RequestParam(value = "org_id") String orgId,
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

        EMRActionReportObject emrActionReportObject = new EMRActionReportObject();
        List<EMRActionCountObject> eventCount = new ArrayList<>();
        List<EMRActionCountObject> userCount = new ArrayList<>();

        EMRActionCountObject shareEventCountObject = new EMRActionCountObject();
        EMRActionCountObject shareUserCountObject = new EMRActionCountObject();

        EMRActionCountObject storeUrlEventCountObject = new EMRActionCountObject();
        EMRActionCountObject storeUrlUserCountObject = new EMRActionCountObject();

        EMRActionCountObject commentEventCountObject = new EMRActionCountObject();
        EMRActionCountObject commentUserCountObject = new EMRActionCountObject();

        DateTime endDay = DateTime.now();

        int[] scanData = eventRepository.scanCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        emrActionReportObject.setScanEventCount(scanData[0]);
        emrActionReportObject.setScanUserCount(scanData[1]);

        // share total count
        int[] shareData = eventRepository.shareCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        shareEventCountObject.setTotalCount(shareData[0]);
        shareUserCountObject.setTotalCount(shareData[1]);

        // share last day count
        int[] shareLastDayData = eventRepository.shareCount(orgId, productBaseId, province, city, endDay.minusHours(24), endDay);
        shareEventCountObject.setLastDayCount(shareLastDayData[0]);
        shareUserCountObject.setLastDayCount(shareLastDayData[1]);

        // share last week count
        int[] shareLastWeekData = eventRepository.shareCount(orgId, productBaseId, province, city, endDay.minusDays(7), endDay);
        shareEventCountObject.setLastWeekCount(shareLastWeekData[0]);
        shareUserCountObject.setLastWeekCount(shareLastWeekData[1]);

        // share last month count
        int[] shareLastMonthData = eventRepository.shareCount(orgId, productBaseId, province, city, endDay.minusMonths(1), endDay);
        shareEventCountObject.setLastMonthCount(shareLastMonthData[0]);
        shareUserCountObject.setLastMonthCount(shareLastMonthData[1]);

        eventCount.add(shareEventCountObject);
        userCount.add(shareUserCountObject);


        // store_url total count
        int[] storeUrlData = eventRepository.storeUrlCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        storeUrlEventCountObject.setTotalCount(storeUrlData[0]);
        storeUrlUserCountObject.setTotalCount(storeUrlData[1]);

        // store_url last day count
        int[] storeUrlLastDayData = eventRepository.storeUrlCount(orgId, productBaseId, province, city, endDay.minusHours(24), endDay);
        storeUrlEventCountObject.setLastDayCount(storeUrlLastDayData[0]);
        storeUrlUserCountObject.setLastDayCount(storeUrlLastDayData[1]);

        // store_url last week count
        int[] storeUrlLastWeekData = eventRepository.storeUrlCount(orgId, productBaseId, province, city, endDay.minusDays(7), endDay);
        storeUrlEventCountObject.setLastWeekCount(storeUrlLastWeekData[0]);
        storeUrlUserCountObject.setLastWeekCount(storeUrlLastWeekData[1]);

        // store_url last month count
        int[] storeUrlLastMonthData = eventRepository.storeUrlCount(orgId, productBaseId, province, city, endDay.minusMonths(1), endDay);
        storeUrlEventCountObject.setLastMonthCount(storeUrlLastMonthData[0]);
        storeUrlUserCountObject.setLastMonthCount(storeUrlLastMonthData[1]);

        eventCount.add(storeUrlEventCountObject);
        userCount.add(storeUrlUserCountObject);

        // comment total count
        int[] commentData = eventRepository.commentCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        commentEventCountObject.setTotalCount(commentData[0]);
        commentUserCountObject.setTotalCount(commentData[1]);

        // comment last day count
        int[] commentLastDayData = eventRepository.commentCount(orgId, productBaseId, province, city, endDay.minusHours(24), endDay);
        commentEventCountObject.setLastDayCount(commentLastDayData[0]);
        commentUserCountObject.setLastDayCount(commentLastDayData[1]);

        // comment last week count
        int[] commentLastWeekData = eventRepository.commentCount(orgId, productBaseId, province, city, endDay.minusDays(7), endDay);
        commentEventCountObject.setLastWeekCount(commentLastWeekData[0]);
        commentUserCountObject.setLastWeekCount(commentLastWeekData[1]);

        // comment last month count
        int[] commentLastMonthData = eventRepository.commentCount(orgId, productBaseId, province, city, endDay.minusMonths(1), endDay);
        commentEventCountObject.setLastMonthCount(commentLastMonthData[0]);
        commentUserCountObject.setLastMonthCount(commentLastMonthData[1]);

        eventCount.add(commentEventCountObject);
        userCount.add(commentUserCountObject);

        emrActionReportObject.setEventCount(eventCount);
        emrActionReportObject.setUserCount(userCount);
        return emrActionReportObject;
    }

    // query user daily event count: share, store_url, comment
    @RequestMapping(value = "/user/event", method = RequestMethod.GET)
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

        List<String[]> scanList = eventRepository.scanDailyCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
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

        List<String[]> shareList = eventRepository.shareDailyCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
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

        List<String[]> storeUrlList = eventRepository.storeUrlDailyCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
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

        List<String[]> commentList = eventRepository.commentDailyCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
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

    // query user  event location count: scan, share, store_url, comment
    @RequestMapping(value = "/user/event/location", method = RequestMethod.GET)
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

        List<String[]> scanList = eventRepository.scanLocationCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        if ((scanList != null) && (scanList.size() > 0)) {
            for (String[] temp : scanList) {
                EMREventLocationReportObject emrEventLocationReportObject = new EMREventLocationReportObject();
                EMREventCountObject emrEventCountObject = new EMREventCountObject();
                emrEventLocationReportObject.setProvince(temp[0]);
                emrEventLocationReportObject.setCity(temp[1]);
                emrEventLocationReportObject.setProductBaseId(temp[2]);

                emrEventCountObject.setScanEventCount(Integer.valueOf(temp[3]));
                emrEventCountObject.setScanUserCount(Integer.valueOf(temp[4]));
                emrEventLocationReportObject.setEvent_count(emrEventCountObject);
                emrEventLocationReportObjectList.add(emrEventLocationReportObject);
            }
        }

        List<String[]> shareList = eventRepository.shareLocationCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        if ((shareList != null) && (shareList.size() > 0)) {
            for (String[] tempShare : shareList) {
                EMREventLocationReportObject emrEventLocationReportObject = new EMREventLocationReportObject();
                EMREventCountObject emrEventCountObject = new EMREventCountObject();
                emrEventLocationReportObject.setProvince(tempShare[0]);
                emrEventLocationReportObject.setCity(tempShare[1]);
                emrEventLocationReportObject.setProductBaseId(tempShare[2]);

                emrEventCountObject.setShareEventCount(Integer.valueOf(tempShare[3]));
                emrEventCountObject.setShareUserCount(Integer.valueOf(tempShare[4]));
                emrEventLocationReportObject.setEvent_count(emrEventCountObject);
                emrEventLocationReportObjectList.add(emrEventLocationReportObject);
            }
        }

        List<String[]> storeUrlList = eventRepository.storeUrlLocationCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        if ((storeUrlList != null) && (storeUrlList.size() > 0)) {
            for (String[] tempStoreUrl : storeUrlList) {
                EMREventLocationReportObject emrEventLocationReportObject = new EMREventLocationReportObject();
                EMREventCountObject emrEventCountObject = new EMREventCountObject();
                emrEventLocationReportObject.setProvince(tempStoreUrl[0]);
                emrEventLocationReportObject.setCity(tempStoreUrl[1]);
                emrEventLocationReportObject.setProductBaseId(tempStoreUrl[2]);

                emrEventCountObject.setStoreUrlEventCount(Integer.valueOf(tempStoreUrl[3]));
                emrEventCountObject.setStoreUrlUserCount(Integer.valueOf(tempStoreUrl[4]));
                emrEventLocationReportObject.setEvent_count(emrEventCountObject);
                emrEventLocationReportObjectList.add(emrEventLocationReportObject);
            }
        }

        List<String[]> commentList = eventRepository.commentLocationCount(orgId, productBaseId, province, city, startDateTime, endDateTime);
        if ((commentList != null) && (commentList.size() > 0)) {
            for (String[] tempComment : commentList) {
                EMREventLocationReportObject emrEventLocationReportObject = new EMREventLocationReportObject();
                EMREventCountObject emrEventCountObject = new EMREventCountObject();
                emrEventLocationReportObject.setProvince(tempComment[0]);
                emrEventLocationReportObject.setCity(tempComment[1]);
                emrEventLocationReportObject.setProductBaseId(tempComment[2]);

                emrEventCountObject.setCommentEventCount(Integer.valueOf(tempComment[3]));
                emrEventCountObject.setCommentUserCount(Integer.valueOf(tempComment[4]));
                emrEventLocationReportObject.setEvent_count(emrEventCountObject);
                emrEventLocationReportObjectList.add(emrEventLocationReportObject);
            }
        }

        return emrEventLocationReportObjectList;
    }


    // 核销管理，关于中奖的数据分析
    @RequestMapping(value = "/market_win_user_location", method = RequestMethod.GET)
    public List<MarketWinUserLocationAnalysisObject> queryMarketUserLocaiton(@RequestParam(value = "marketing_id") String marketingId) {
        List<MarketUserLocationAnalysisEntity> list = eventRepository.queryRewardLocationReport(marketingId);

        Map<String, Integer> provinceData = list.stream().collect(
                Collectors.groupingBy(MarketUserLocationAnalysisEntity::getProvince,
                        Collectors.summingInt(MarketUserLocationAnalysisEntity::getCount)));

        return provinceData.entrySet().stream().map(i -> {
            MarketWinUserLocationAnalysisObject provinceItem = new MarketWinUserLocationAnalysisObject();
            provinceItem.setName(i.getKey());
            provinceItem.setValue(i.getValue());

            List<MarketWinUserLocationAnalysisObject> cityData = list.stream().filter(l -> l.getProvince().equals(i.getKey())).map(ii -> {
                MarketWinUserLocationAnalysisObject city = new MarketWinUserLocationAnalysisObject();
                city.setName(ii.getCity());
                city.setValue(ii.getCount());
                return city;
            }).collect(Collectors.toList());
            provinceItem.setCityData(cityData);
            return provinceItem;
        }).collect(Collectors.toList());
    }


}
