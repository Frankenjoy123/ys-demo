package com.yunsoo.service;

import com.yunsoo.service.contract.ScanRecord;

import java.util.List;

/**
 * Created by Zhe on 2015/2/4.
 */
public interface ScanRecordService {
    public ScanRecord get(long id);

    public long save(ScanRecord scanRecord);

    public List<ScanRecord> getScanRecordsByFilter(String productKey, Integer baseProductId, Integer userId, String createdDateTime, int pageIndex, int pageSize);
}
