package com.yunsoo.di.api.controller;

import com.yunsoo.di.dto.DailyJobReport;
import com.yunsoo.di.dto.JobLog;
import com.yunsoo.di.service.JobService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yqy09_000 on 2016/9/10.
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    JobService jobService;

    @RequestMapping(value = "health_check", method = RequestMethod.GET)
    public List<JobLog> healthCheck()
    {
        return jobService.getJobStatus();
    }

    @RequestMapping(value = "daily_report", method = RequestMethod.GET)
    public List<DailyJobReport> getDailyJobReport(  @RequestParam(value = "date_from")
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate dateFrom,
                           @RequestParam(value = "date_end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate dateEnd)
    {
        DateTime startDateTime = dateFrom.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
        DateTime endDateTime = dateEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);
        return jobService.getDailyJobReport(startDateTime, endDateTime);

    }
}
