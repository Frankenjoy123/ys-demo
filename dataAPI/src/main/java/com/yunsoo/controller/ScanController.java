package com.yunsoo.controller;

import com.yunsoo.dto.ResultWrapper;
import com.yunsoo.factory.ResultFactory;
import com.yunsoo.service.ScanRecordService;
import com.yunsoo.service.contract.ScanRecord;
import org.joda.time.DateTime;
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

    @RequestMapping(value = "/filterlog", method = RequestMethod.GET)
    public ResponseEntity<List<ScanRecord>> getScanRecordsByFilter(@RequestParam(value = "productKey", required = false) String productKey,
                                                   @RequestParam(value = "baseProductId", required = false) Integer baseProductId,
                                                   @RequestParam(value = "userId", required = false) Integer userId,
                                                   @RequestParam(value = "createdDateTime", required = false, defaultValue = "") String createdDateTime,
                                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        List<ScanRecord> scanRecordList = scanRecordService.getScanRecordsByFilter(productKey, baseProductId, userId, createdDateTime,
                pageIndex, pageSize);
        return new ResponseEntity(scanRecordList, HttpStatus.OK);
    }

    @RequestMapping(value = "/savelog", method = RequestMethod.POST)
    public ResponseEntity<ResultWrapper> createScanRecord(@RequestBody ScanRecord scanRecord) {
        long id = scanRecordService.save(scanRecord);
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(id), HttpStatus.CREATED);
    }
}
