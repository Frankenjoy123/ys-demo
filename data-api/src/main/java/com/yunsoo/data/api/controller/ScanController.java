package com.yunsoo.data.api.controller;

import com.yunsoo.data.service.service.ScanRecordService;
import com.yunsoo.data.service.service.contract.ScanRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.yunsoo.common.data.object.ScanRecordObject;

import java.util.ArrayList;
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ScanRecordObject> getNewMessagesByMessageId(@PathVariable(value = "id") Long id) {
        return new ResponseEntity(this.FromScanRecord(scanRecordService.get(id)), HttpStatus.OK);
    }

    //General 的扫描记录filter
    @RequestMapping(value = "/filterby", method = RequestMethod.GET)
    public List<ScanRecordObject> getScanRecordsByFilter(@RequestParam(value = "productKey", required = false) String productKey,
                                                   @RequestParam(value = "baseProductId", required = false) Integer baseProductId,
                                                   @RequestParam(value = "userId", required = false) String userId,
                                                   @RequestParam(value = "createdDateTime", required = false, defaultValue = "") String createdDateTime,
                                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        List<ScanRecord> scanRecordList = scanRecordService.getScanRecordsByFilter(productKey, baseProductId, userId, createdDateTime,
                pageIndex, pageSize);
        return this.FromScanRecordList(scanRecordList);
    }

    //找用户的扫描记录，根据某记录的id向前或者向后搜寻
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public List<ScanRecordObject> filterScanRecords(@RequestParam(value = "Id", required = false) Long Id,
                                              @RequestParam(value = "userId", required = false) String userId,
                                              @RequestParam(value = "backward", required = false) Boolean backward,
                                              @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        List<ScanRecord> scanRecordList = scanRecordService.filterScanRecords(Id, userId, backward, pageIndex, pageSize);
        return this.FromScanRecordList(scanRecordList);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long createScanRecord(@RequestBody ScanRecordObject scanRecordObject) {
        ScanRecord scanRecord = this.ToScanRecord(scanRecordObject);
        long id = scanRecordService.save(scanRecord);
        return id;
    }


    private ScanRecordObject FromScanRecord(ScanRecord scanRecord) {
        ScanRecordObject scanRecordObject = new ScanRecordObject();
        BeanUtils.copyProperties(scanRecord, scanRecordObject);
        return scanRecordObject;
    }

    private ScanRecord ToScanRecord(ScanRecordObject scanRecordObject) {
        ScanRecord scanRecord = new ScanRecord();
        BeanUtils.copyProperties(scanRecordObject, scanRecord);
        return scanRecord;
    }

    private List<ScanRecordObject> FromScanRecordList(List<ScanRecord> scanRecordList) {
        if (scanRecordList == null) return null;

        List<ScanRecordObject> scanRecordObjectList = new ArrayList<>();
        for (ScanRecord scanRecord : scanRecordList) {
            scanRecordObjectList.add(this.FromScanRecord(scanRecord));
        }
        return scanRecordObjectList;
    }

    private List<ScanRecord> ToScanRecordList(List<ScanRecordObject> scanRecordObjectList) {
        if (scanRecordObjectList == null) return null;

        List<ScanRecord> scanRecordList = new ArrayList<>();
        for (ScanRecordObject scanRecordObject : scanRecordObjectList) {
            scanRecordList.add(this.ToScanRecord(scanRecordObject));
        }
        return scanRecordList;
    }
    
}
