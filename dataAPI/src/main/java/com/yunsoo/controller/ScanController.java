package com.yunsoo.controller;

import com.yunsoo.service.ScanRecordService;
import com.yunsoo.service.contract.ScanRecord;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/2/4.
 */
@RestController
@RequestMapping("/scan")
public class ScanController {

    @Autowired
    private final ScanRecordService scanRecordService;

    @Autowired
    ScanController(ScanRecordService scanRecordService) {
        this.scanRecordService = scanRecordService;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public ScanRecord getNewMessagesByMessageId(@PathVariable(value = "id") Integer id) {
        return scanRecordService.get(id);
    }

    @RequestMapping(value = "/filterlog", method = RequestMethod.GET)
    public List<ScanRecord> getScanRecordsByFilter(@RequestParam(value = "productKey", required = false) String productKey,
                                                   @RequestParam(value = "baseProductId", required = false) Integer baseProductId,
                                                   @RequestParam(value = "userId", required = false) Integer userId,
                                                   @RequestParam(value = "createdDateTime", required = false, defaultValue = "") String createdDateTime,
                                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        List<ScanRecord> scanRecordList = scanRecordService.getScanRecordsByFilter(productKey, baseProductId, userId, createdDateTime,
                pageIndex, pageSize);
        return scanRecordList;
    }

    @RequestMapping(value = "/savelog", method = RequestMethod.POST)
    public long createMessages(ScanRecord scanRecord) {
        long id = scanRecordService.save(scanRecord);
        return id;
    }
}
