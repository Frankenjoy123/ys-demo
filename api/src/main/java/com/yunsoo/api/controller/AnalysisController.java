package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AnalysisDomain;
import com.yunsoo.api.dto.KeyUsageReport;
import com.yunsoo.api.dto.ScanAnalysisReport;
import com.yunsoo.api.dto.ScanLocationAnalysisReport;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ScanRecordAnalysisObject;
import com.yunsoo.common.data.object.ScanRecordLocationAnalysisObject;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Dake Wang on 2016/2/15.
 */
@RestController
@RequestMapping("/bigdata")
public class AnalysisController {
    @Autowired
    private AnalysisDomain analysisDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;


    @RequestMapping(value = "/scan_report", method = RequestMethod.GET)
    public ScanAnalysisReport getScanAnalysisReport(@RequestParam(value = "start_time", required = false) org.joda.time.LocalDate startTime,
                                                    @RequestParam(value = "end_time", required = false) org.joda.time.LocalDate endTime,
                                                    @RequestParam(value = "product_base_id", required = false) String productBaseId, @RequestParam(value = "batch_id", required = false) String batchId
    ) {
        //TODO for test purpose
        LocalDate now = LocalDate.now();
        if (startTime == null) {
            startTime = now.plusDays(-500);
        }
        if (endTime == null) {
            endTime = now;
        }
        String orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        //String orgId = "2k0r1l55i2rs5544wz5";
        List<ScanRecordAnalysisObject> data = analysisDomain.getScanAnalysisReport(orgId, startTime, endTime, productBaseId, batchId);
        Map<DateTime, Integer> pvData = data.stream().collect(Collectors.groupingBy(ScanRecordAnalysisObject::getScanDate, Collectors.summingInt(ScanRecordAnalysisObject::getPv)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,  (k,v)->{throw new IllegalStateException();}, LinkedHashMap::new));
        Map<DateTime, Integer> uvData = data.stream().collect(Collectors.groupingBy(ScanRecordAnalysisObject::getScanDate, Collectors.summingInt(ScanRecordAnalysisObject::getUv)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k,v)->{throw new IllegalStateException();}, LinkedHashMap::new));;


        ScanAnalysisReport report = new ScanAnalysisReport();
        ScanAnalysisReport.PVUV pvuv = new ScanAnalysisReport.PVUV();

        pvuv.setPv(pvData.values().stream().mapToInt(Integer::intValue).toArray());
        pvuv.setUv(uvData.values().stream().mapToInt(Integer::intValue).toArray());

        report.setData(pvuv);
        report.setDate(pvData.keySet().stream().map(dt -> dt.toString("yyyy-MM-dd")).sorted().collect(Collectors.toList()).toArray(new String[0]));

        return report;

    }

    @RequestMapping(value = "/scan_location_report", method = RequestMethod.GET)
    public ScanLocationAnalysisReport getScanLocationAnalysisReport(@RequestParam(value = "start_time", required = false) org.joda.time.LocalDate startTime,
                                                                    @RequestParam(value = "end_time", required = false) org.joda.time.LocalDate endTime,
                                                                    @RequestParam(value = "product_base_id", required = false) String productBaseId, @RequestParam(value = "batch_id", required = false) String batchId
    ) {
        //TODO for test purpose
        LocalDate now = LocalDate.now();
        if (startTime == null) {
            startTime = now.plusDays(-500);
        }
        if (endTime == null) {
            endTime = now;
        }
        String orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        //String orgId = "2k0r1l55i2rs5544wz5";
        List<ScanRecordLocationAnalysisObject> data = analysisDomain.getScanLocationAnalysisReport(orgId, startTime, endTime, productBaseId, batchId);

        ScanLocationAnalysisReport report = new ScanLocationAnalysisReport();
        Map<String, Integer> provinceData = data.stream().collect(Collectors.groupingBy(ScanRecordLocationAnalysisObject::getProvince, Collectors.summingInt(ScanRecordLocationAnalysisObject::getPv)));

        ScanLocationAnalysisReport.NameValue[] nvProvinceData = provinceData.entrySet().stream().map(e -> {
            ScanLocationAnalysisReport.NameValue nv = new ScanLocationAnalysisReport.NameValue();
            nv.setName(e.getKey());
            nv.setValue(e.getValue());
            Map<String, Integer> cityData = data.stream().filter(p -> p.getProvince().equals(e.getKey())).collect(Collectors.groupingBy(ScanRecordLocationAnalysisObject::getCity, Collectors.summingInt(ScanRecordLocationAnalysisObject::getPv)));
            ScanLocationAnalysisReport.NameValue[] nvCityData = cityData.entrySet().stream().map(c -> {
                ScanLocationAnalysisReport.NameValue city = new ScanLocationAnalysisReport.NameValue();
                city.setName(c.getKey());
                city.setValue(c.getValue());
                return city;
            }).collect(Collectors.toList()).toArray(new ScanLocationAnalysisReport.NameValue[0]);
            nv.setCityData(nvCityData);

            return nv;
        }).collect(Collectors.toList()).toArray(new ScanLocationAnalysisReport.NameValue[0]);

        report.setProvinceData(nvProvinceData);

        return report;
    }

    @RequestMapping(value = "/key_usage_report", method = RequestMethod.GET)
    public KeyUsageReport getKeyUsageReport(@RequestParam(value = "start_time", required = false) org.joda.time.LocalDate startTime,
                                            @RequestParam(value = "end_time", required = false) org.joda.time.LocalDate endTime,
                                            @RequestParam(value = "product_base_id", required = false) String productBaseId) {
        //TODO for test purpose
        LocalDate now = LocalDate.now();
        if (startTime == null) {
            startTime = now.plusDays(-500);
        }
        if (endTime == null) {
            endTime = now;
        }
        String orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        //String orgId = "2k0r1l55i2rs5544wz5";
        List<ProductKeyBatchObject> list = analysisDomain.getDailyKeyUsageReport(orgId, productBaseId, startTime, endTime);
        Map<String, Integer> quantityMap = list.stream()
                .collect(Collectors.groupingBy(ProductKeyBatchObject::getCreatedDateString, Collectors.summingInt(ProductKeyBatchObject::getQuantity)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException();
                }, LinkedHashMap::new));

        Map<String, Integer> restQuantityMap = list.stream()
                .collect(Collectors.groupingBy(o->o.getCreatedDateTime().toString("yyyy-MM-dd"), Collectors.summingInt(ProductKeyBatchObject::getRestQuantity)))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
                    throw new IllegalStateException();
                }, LinkedHashMap::new));

        KeyUsageReport report = new KeyUsageReport();
        report.setDate(quantityMap.keySet().toArray(new String[0]));
        report.setQuantity(quantityMap.values().stream().mapToInt(Integer::intValue).toArray());
        report.setRestQuantity(restQuantityMap.values().stream().mapToInt(Integer::intValue).toArray());

        return report;


    }



}
