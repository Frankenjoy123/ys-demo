package com.yunsoo.di.api.controller;

import com.yunsoo.di.dao.entity.*;
import com.yunsoo.di.dao.repository.*;
import com.yunsoo.di.dto.*;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xiaowu on 2016/10/26.
 */

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Autowired
    private ScanRecordLocationAnalysisRepository scanRecordLocationAnalysisRepository;

    @Autowired
    private ScanRecordAnalysisRepository scanRecordAnalysisRepository;

    // region 新增营销方案的repository
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
    // endregion

    //S 消费者漏斗分析
    @Autowired
    private EMREventRepository eventRepository;

    // 用户属性分析
    @Autowired
    private EMRUserRepository emrUserRepository;


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


    //TODO 需要修改
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

        List<ScanRecordLocationAnalysisEntity> list = eventRepository.consumerLocationCount(orgId, productBaseId, batchId, startDateTime, endDateTime);
        return list.stream().map(ScanRecordLocationAnalysisEntity::toDataObject).collect(Collectors.toList());
    }


    //TODO 需要修改
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


}
