package com.yunsoo.dataapi.controller;

import com.yunsoo.service.ScanRecordService;
import com.yunsoo.service.contract.ScanRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ScanRecord> getNewMessagesByMessageId(@PathVariable(value = "id") Integer id) {
        return new ResponseEntity(scanRecordService.get(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/filterby", method = RequestMethod.GET)
    public List<ScanRecord> getScanRecordsByFilter(@RequestParam(value = "productKey", required = false) String productKey,
                                                   @RequestParam(value = "baseProductId", required = false) Integer baseProductId,
                                                   @RequestParam(value = "userId", required = false) Long userId,
                                                   @RequestParam(value = "createdDateTime", required = false, defaultValue = "") String createdDateTime,
                                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        List<ScanRecord> scanRecordList = scanRecordService.getScanRecordsByFilter(productKey, baseProductId, userId, createdDateTime,
                pageIndex, pageSize);
        return scanRecordList;
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public List<ScanRecord> filterScanRecords(@RequestParam(value = "Id", required = false) Long Id,
                                              @RequestParam(value = "userId", required = false) Long userId,
                                              @RequestParam(value = "backward", required = false) Boolean backward,
                                              @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        List<ScanRecord> scanRecordList = scanRecordService.filterScanRecords(Id, userId, backward, pageIndex, pageSize);
        return scanRecordList;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long createScanRecord(@RequestBody ScanRecord scanRecord) {
        long id = scanRecordService.save(scanRecord);
        return id;
    }
}
