package com.yunsoo.di.api.controller;

import com.yunsoo.di.dto.ProductKeyBatch;
import com.yunsoo.di.service.KeyService;
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

/**
 * Created by yqy09_000 on 2016/10/31.
 */
@RestController
@RequestMapping("/key")
public class KeyController {

    @Autowired
    KeyService keyService;

    @RequestMapping(value = "daily_usage_report", method = RequestMethod.GET)
    public List<ProductKeyBatch> getDailyUsageReport(@RequestParam(value = "org_id") String orgId,
                                                     @RequestParam(value = "start_time")
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startDate,
                                                     @RequestParam(value = "end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endDate,
                                                     @RequestParam(value = "product_base_id", required = false) String productBaseId)
    {
        DateTime startDateTime = startDate.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = endDate.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);
        if(StringUtils.isEmpty(productBaseId))
            productBaseId = null;
        return keyService.getDailyKeyUsageReport(orgId,productBaseId,startDateTime, endDateTime);
    }
}
