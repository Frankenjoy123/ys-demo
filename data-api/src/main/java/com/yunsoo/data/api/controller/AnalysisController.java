package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ScanRecordAnalysisObject;
import com.yunsoo.common.data.object.ScanRecordLocationAnalysisObject;
import com.yunsoo.data.service.entity.ProductKeyBatchEntity;
import com.yunsoo.data.service.entity.ScanRecordAnalysisEntity;
import com.yunsoo.data.service.entity.ScanRecordLocationAnalysisEntity;
import com.yunsoo.data.service.repository.ProductKeyBatchRepository;
import com.yunsoo.data.service.repository.ScanRecordAnalysisRepository;
import com.yunsoo.data.service.repository.ScanRecordLocationAnalysisRepository;
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

        List<ScanRecordAnalysisEntity> list = scanRecordAnalysisRepository.query(orgId, startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)), endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)), productBaseId, batchId);
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

        List<ScanRecordLocationAnalysisEntity> list = scanRecordLocationAnalysisRepository.query(orgId,  startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)), endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)), productBaseId, batchId);
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

        List<ProductKeyBatchEntity> list = productKeyBatchRepository.queryDailyKeyUsageReport(orgId, productBaseId,startDateTime,endDateTime);
        return list.stream().map(ProductKeyBatchEntity::toDataObject).collect(Collectors.toList());
    }


}
