package com.yunsoo.di.service;

import com.yunsoo.di.dao.entity.DailyJobReportEntity;
import com.yunsoo.di.dao.entity.JobEntity;
import com.yunsoo.di.dao.repository.JobRepository;
import com.yunsoo.di.dto.DailyJobReport;
import com.yunsoo.di.dto.JobLog;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yqy09_000 on 2016/9/10.
 */
@Service
public class JobService {

    @Autowired
    JobRepository jobRepository;


    public List<JobLog> getJobStatus()
    {
       List<JobEntity> entities =  jobRepository.queryLatestJobs();
       return entities.stream().map(JobService::toJobLog).collect(Collectors.toList());
    }


    public List<DailyJobReport> getDailyJobReport(DateTime startDateTime, DateTime endDateTime) {
        List<DailyJobReportEntity> entities =  jobRepository.queryDailyJobReport(startDateTime, endDateTime);
        return entities.stream().map(JobService::toDailyJobReport).collect(Collectors.toList());
    }


    private static JobLog toJobLog(JobEntity entity) {
        JobLog log = new JobLog();
        log.setName(entity.getName());
        log.setStatus(entity.getStatus());
        log.setEndDateTime(entity.getJobEndDateTime());
        log.setStartDateTime(entity.getJobStartDateTime());
        log.setErrorMsg(entity.getErrorMsg());
        DateTime jobStart = log.getStartDateTime();
        DateTime jobEnd = log.getEndDateTime();
        if (jobEnd != null) {
            Duration dr = new Duration(jobStart, jobEnd);
            log.setExecuteDuration((int)dr.getStandardSeconds());
        }
        return log;
    }

    private static DailyJobReport toDailyJobReport(DailyJobReportEntity entity)
    {
        DailyJobReport report = new DailyJobReport();
        report.setDate(entity.getDate().toString("yyyy-MM-dd"));
        report.setJobName(entity.getJobName());
        report.setSucceedCount(entity.getSucceedCount());
        report.setFailedCount(entity.getFailedCount());
        return report;
    }
}
