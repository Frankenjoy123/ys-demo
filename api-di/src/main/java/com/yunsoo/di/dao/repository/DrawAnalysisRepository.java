package com.yunsoo.di.dao.repository;

import com.yunsoo.di.dao.entity.DrawReportEntity;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by yqy09_000 on 2016/11/14.
 */
public interface DrawAnalysisRepository {
    public List<DrawReportEntity> getDrawReportBy(String orgId, String marketingId,boolean bypass, DateTime startDateTime, DateTime endDateTime);

    public List<DrawReportEntity> getDrawPrizeRankBy( String marketingId, DateTime startDateTime, DateTime endDateTime);

    List<DrawReportEntity> getMappedDrawReportReportBy(String marketingId, DateTime startDateTime, DateTime endDateTime);
}
