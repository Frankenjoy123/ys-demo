package com.yunsoo.di.dao.repository;

import com.yunsoo.di.dao.entity.ScanRecordLocationAnalysisEntity;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by:   xiaowu
 * Created on:   2016/11/1
 * Descriptions:
 */
public interface ScanRecordRepository {

    List<ScanRecordLocationAnalysisEntity> consumerLocationCount(String orgId, String productBaseId, String batchId, DateTime startDateTime, DateTime endDateTime);

}
