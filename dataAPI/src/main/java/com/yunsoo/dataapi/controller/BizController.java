package com.yunsoo.dataapi.controller;

import com.yunsoo.dataapi.dto.ScanRecordDto;
import com.yunsoo.dataapi.dto.ScanResultDto;
import com.yunsoo.service.ScanRecordService;
import com.yunsoo.service.contract.ScanRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Zhe on 2015/2/12.
 * <p>
 * To-be moved into Business API layer which consumes DataAPI
 */
@RestController
@RequestMapping("/productscan")
public class BizController {

    @Autowired
    private final ScanRecordService scanRecordService;

    @Autowired
    BizController(ScanRecordService scanRecordService) {
        this.scanRecordService = scanRecordService;
    }

    @RequestMapping(value = "/get/{key}", method = RequestMethod.GET)
    public ResponseEntity<ScanResultDto> getNewMessagesByMessageId(@PathVariable(value = "key") String key) {
        ScanResultDto scanResultDto = new ScanResultDto();
        scanResultDto.setKeyType("new key");
        scanResultDto.setLogisticsDto(null);
        scanResultDto.setManufacturer(null);
        scanResultDto.setProduct(null);

        List<ScanRecord> scanRecordList = scanRecordService.getScanRecordsByFilter(key, null, null, null,
                0, 100);
        scanResultDto.setScanRecord(ScanRecordDto.FromScanRecordList(scanRecordList));
        return new ResponseEntity(scanResultDto, HttpStatus.OK);
    }
}
