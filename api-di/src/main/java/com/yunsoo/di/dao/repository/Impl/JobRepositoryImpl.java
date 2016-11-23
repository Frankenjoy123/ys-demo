package com.yunsoo.di.dao.repository.Impl;

import com.yunsoo.di.dao.entity.DailyJobReportEntity;
import com.yunsoo.di.dao.entity.JobEntity;
import com.yunsoo.di.dao.repository.JobRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yqy09_000 on 2016/10/26.
 */
@Repository
public class JobRepositoryImpl implements JobRepository {


    @PersistenceContext(unitName = "di")
    private EntityManager entityManager;

    @Override
    public List<JobEntity> queryLatestJobs() {
       String sql = "SELECT job.JOBNAME, job.STATUS, job.ERRORS, job.REPLAYDATE, job.LOGDATE, \n" +
               "case when job.ERRORS > 0 then job.LOG_FIELD else '' end as 'error_msg' \n" +
               "FROM di.job_log job inner join (select max(ID_JOB) as id from job_log group by JOBNAME) as LatestJob\n" +
               "on job.ID_JOB = LatestJob.id order by job.REPLAYDATE";
        List<Object[]> data =  entityManager.createNativeQuery(sql).getResultList();
        List<JobEntity> list = new ArrayList<>();
        for (Object[] d : data) {
            JobEntity entity = new JobEntity();
            entity.setName((String) d[0]);
            entity.setStatus((String) d[1]);
            entity.setErrors(((Number) d[2]).intValue());
            Timestamp ts = (Timestamp)d[3];
            entity.setJobStartDateTime(new DateTime(ts.getTime()));
            if(d[4] != null ){
                Timestamp tsEnd = (Timestamp)d[4];
                entity.setJobEndDateTime(new DateTime(tsEnd.getTime()));
            }
            entity.setErrorMsg((String) d[5]);
            list.add(entity);
        }
        return list;

    }

    @Override
    public List<DailyJobReportEntity> queryDailyJobReport(DateTime startDateTime, DateTime endDateTime) {
        String sql = "select date(convert_tz(REPLAYDATE,'+00:00','+08:00')) as date, JOBNAME, sum(case when ERRORS = 0 then 1 else 0 end) as 'succeeded_count', \n" +
                "sum(case when ERRORS > 0 then 1 else 0 end) as 'failed_count' from job_log where REPLAYDATE >=:ds and REPLAYDATE <:de group by date, JOBNAME\n" +
                "order by date";

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("ds", startDateTime.toString("yyyy-MM-dd"));
        parameters.put("de", endDateTime.toString("yyyy-MM-dd"));
        Query query =  entityManager.createNativeQuery(sql);
        for (String key : parameters.keySet()) {
            query.setParameter(key, parameters.get(key));
        }
        List<Object[]> data = query.getResultList();
        List<DailyJobReportEntity> list = new ArrayList<>();
        for (Object[] d : data) {
            DailyJobReportEntity entity = new DailyJobReportEntity();
            entity.setDate(new LocalDate(((Date)d[0]).getTime()));
            entity.setJobName((String)d[1]);
            entity.setSucceedCount(((Number) d[2]).intValue());
            entity.setFailedCount(((Number) d[3]).intValue());
            list.add(entity);
        }
        return list;

    }
}
