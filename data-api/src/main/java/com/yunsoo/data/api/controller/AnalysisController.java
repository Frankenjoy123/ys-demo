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

import java.util.List;
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

    @Autowired
    private ProductKeyBatchRepository productKeyBatchRepository;


    @RequestMapping(value = "/scan_data", method = RequestMethod.GET)
    public List<ScanRecordAnalysisObject> query(@RequestParam(value = "org_id") String orgId,
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
        DateTime endDateTime =  endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<ScanRecordAnalysisEntity> list = scanRecordAnalysisRepository.query(orgId, startDateTime, endDateTime, productBaseId, batchId);
        return list.stream().map(ScanRecordAnalysisEntity::toDataObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "/scan_data_location", method = RequestMethod.GET)
    public List<ScanRecordLocationAnalysisObject> queryWithLocation(@RequestParam(value = "org_id") String orgId,
                                                                    @RequestParam(value = "start_time")
                                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                                                    @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                                                    @RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                                    @RequestParam(value = "batch_id",required = false) String batchId) {
        if (StringUtils.isEmpty(productBaseId))
            productBaseId = null;
        if (StringUtils.isEmpty(batchId))
            batchId = null;

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime =  endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<ScanRecordLocationAnalysisEntity> list = scanRecordLocationAnalysisRepository.query(orgId,  startDateTime, endDateTime, productBaseId, batchId);
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
        DateTime endDateTime =  endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

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
        DateTime endDateTime =  endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

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
        DateTime endDateTime =  endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

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
        DateTime endDateTime =  endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

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
        DateTime endDateTime =  endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

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
        DateTime endDateTime =  endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

        List<MarketUserLocationAnalysisEntity> list = marketUserLocationAnalysisRepository.query(orgId, startDateTime, endDateTime, marketingId);
        return list.stream().map(MarketUserLocationAnalysisEntity::toDataObject).collect(Collectors.toList());
    }
    // TODO  新增营销报表，用户地域分析
    @RequestMapping(value = "/market_user_tags", method = RequestMethod.GET)
    public List<LuTagObject> queryMarketUserTag() {

        List<LuTagEntity> list = luTagRepository.findAll();
        return list.stream().map(LuTagEntity::toDataObject).collect(Collectors.toList());
    }



}
