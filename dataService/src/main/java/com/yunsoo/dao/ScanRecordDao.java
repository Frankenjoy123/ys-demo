package com.yunsoo.dao;

import com.yunsoo.dbmodel.ScanRecordModel;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Zhe on 2015/2/4.
 */
public interface ScanRecordDao {
    public ScanRecordModel get(long id);

    public long save(ScanRecordModel scanRecordModel);

    public List<ScanRecordModel> getScanRecordsByFilter(String productKey, Integer baseProductId, Integer userId, DateTime createdDateTime, int pageIndex, int pageSize);
}
