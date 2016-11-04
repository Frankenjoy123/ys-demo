package com.yunsoo.di.dao.repository;


import com.yunsoo.di.dao.entity.DailyJobReportEntity;
import com.yunsoo.di.dao.entity.JobEntity;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by:   qiyong
 * Created on:   2016/9/10
 * Descriptions:
 */
public interface JobRepository{

    List<JobEntity> queryLatestJobs();


    List<DailyJobReportEntity> queryDailyJobReport(DateTime startDateTime, DateTime endDateTime);
}
