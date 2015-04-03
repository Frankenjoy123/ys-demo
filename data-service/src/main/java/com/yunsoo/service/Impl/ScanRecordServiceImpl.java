package com.yunsoo.service.Impl;

import com.yunsoo.dao.ScanRecordDao;
import com.yunsoo.service.ScanRecordService;
import com.yunsoo.service.contract.ScanRecord;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Zhe on 2015/2/4.
 */
@Service("scanRecordService")
public class ScanRecordServiceImpl implements ScanRecordService {

    @Autowired
    private ScanRecordDao scanRecordDao;

    @Override
    public ScanRecord get(long id) {
        return ScanRecord.FromModel(scanRecordDao.get(id));
    }

    @Override
    public long save(ScanRecord scanRecord) {
        if (scanRecord == null) {
            return -1L;
        }
        return scanRecordDao.save(ScanRecord.ToModel(scanRecord));
    }

    @Override
    public List<ScanRecord> getScanRecordsByFilter(String productKey, Integer baseProductId, Long userId, String createdDateTime, int pageIndex, int pageSize) {
        DateTime theCreatedDateTime = null;
        if (createdDateTime.isEmpty()) {
            theCreatedDateTime = null;
        } else {
            theCreatedDateTime = DateTime.parse(createdDateTime);
        }
        return ScanRecord.FromModelList(scanRecordDao.getScanRecordsByFilter(productKey, baseProductId, userId, theCreatedDateTime, pageIndex, pageSize));
    }

    @Override
    public List<ScanRecord> filterScanRecords(Long Id, Long userId, Boolean getOlder, int pageIndex, int pageSize) {
        return ScanRecord.FromModelList(scanRecordDao.filterScanRecords(Id, userId, getOlder, pageIndex, pageSize));
    }
}
