package com.yunsoo.api.controller.analysis;

import com.yunsoo.api.di.service.KeyAnalysisService;
import com.yunsoo.api.di.service.ScanAnalysisService;
import com.yunsoo.api.dto.KeyUsageReport;
import com.yunsoo.api.dto.ScanAnalysisReport;
import com.yunsoo.api.dto.ScanLocationAnalysisReport;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ScanRecordAnalysisObject;
import com.yunsoo.common.data.object.ScanRecordLocationAnalysisObject;
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
 * Created by xiaowu on 2016/11/18.
 */
@RestController
@RequestMapping("/bigdata2")
public class KeyAnalysisController {
    @Autowired
    private KeyAnalysisService keyAnalysisService;

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
        List<ProductKeyBatchObject> list = keyAnalysisService.getDailyKeyUsageReport(orgId, productBaseId, startTime, endTime);
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

}
