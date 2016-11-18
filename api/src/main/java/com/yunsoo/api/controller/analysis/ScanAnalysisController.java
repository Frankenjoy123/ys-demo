package com.yunsoo.api.controller.analysis;

import com.yunsoo.api.di.dto.DrawAnalysisReport;
import com.yunsoo.api.di.service.DrawAnalysisService;
import com.yunsoo.api.di.service.ScanAnalysisService;
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
 * Created by Xiaowu on 2016/11/18.
 */
@RestController
@RequestMapping("/bigdata2")
public class ScanAnalysisController {
    @Autowired
    private ScanAnalysisService scanAnalysisService;

    // region 数据分析
    @RequestMapping(value = "/scan_report", method = RequestMethod.GET)
    public ScanAnalysisReport getScanAnalysisReport(@RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startTime,
                                                    @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endTime,
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

        List<ScanRecordAnalysisObject> data = scanAnalysisService.getScanAnalysisReport(orgId, startTime, endTime, productBaseId, batchId);

        Map<DateTime, ScanRecordAnalysisObject> mapData =  data.stream().collect(Collectors.toMap(ScanRecordAnalysisObject::getScanDate, p->p, (k, v) -> {
            throw new IllegalStateException();
        }, LinkedHashMap::new));

        DateTime startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(0));
        DateTime endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(0)).plusMinutes(10);


        List<Integer> pvList = new ArrayList<>();
        List<Integer> uvList = new ArrayList<>();
        List<Integer> firstScanList = new ArrayList<>();
        List<String> dateList = new ArrayList<String>();

        do {
            int pv = 0;
            int uv = 0;
            int firstScan= 0;
            if (mapData.containsKey(startDateTime)) {
                ScanRecordAnalysisObject object =  mapData.get(startDateTime);
                pv = object.getPv();
                uv = object.getUv();
                firstScan = object.getFirstScan();
            }
            pvList.add(pv);
            uvList.add(uv);
            firstScanList.add(firstScan);
            dateList.add(startDateTime.toString("yyyy-MM-dd"));
            startDateTime = startDateTime.plusDays(1);

        } while (startDateTime.isBefore(endDateTime));


        ScanAnalysisReport report = new ScanAnalysisReport();
        ScanAnalysisReport.PVUV pvuv = new ScanAnalysisReport.PVUV();

        pvuv.setPv(pvList.stream().mapToInt(Integer::intValue).toArray());
        pvuv.setUv(uvList.stream().mapToInt(Integer::intValue).toArray());
        pvuv.setFirstScan(firstScanList.stream().mapToInt(Integer::intValue).toArray());
        report.setData(pvuv);
        report.setDate(dateList.toArray(new String[0]));
        return report;
    }

    @RequestMapping(value = "/scan_location_report", method = RequestMethod.GET)
    public ScanLocationAnalysisReport getScanLocationAnalysisReport(@RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startTime,
                                                                    @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endTime,
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
        List<ScanRecordLocationAnalysisObject> data = scanAnalysisService.getScanLocationAnalysisReport(orgId, startTime, endTime, productBaseId, batchId);

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

}
