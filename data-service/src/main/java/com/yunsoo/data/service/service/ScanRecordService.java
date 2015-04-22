package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.ScanRecord;

import java.util.List;

/**
 * Created by Zhe on 2015/2/4.
 */
public interface ScanRecordService {
    public ScanRecord get(long id);

    public long save(ScanRecord scanRecord);

    public List<ScanRecord> getScanRecordsByFilter(String productKey, Integer baseProductId, String userId, String createdDateTime, int pageIndex, int pageSize);

    public List<ScanRecord> filterScanRecords(Long Id, String userId, Boolean getOlder, int pageIndex, int pageSize);
}
