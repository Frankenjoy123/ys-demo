package com.yunsoo.key.api.controller;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.key.dto.Key;
import com.yunsoo.key.dto.ProductTrace;
import com.yunsoo.key.service.KeyService;
import com.yunsoo.key.service.ProductPackageService;
import com.yunsoo.key.service.ProductTraceService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Created by:   yan
 * Created on:   10/11/2016
 * Descriptions:
 */
@RestController
@RequestMapping("producttrace")
public class ProductTraceController {

    @Autowired
    ProductTraceService service;

    @Autowired
    ProductPackageService packageService;

    @Autowired
    KeyService keyService;

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public List<ProductTrace> getProductTraceByKey(@PathVariable("key") String key) {
        List<String> keyList = packageService.getAllParentKeysByKey(key);
        keyList.add(key);
        return service.getProductTraceListByKeys(keyList);
    }

    @RequestMapping(value = "/external/{partitionId}/{externalKey}", method = RequestMethod.GET)
    public List<ProductTrace> getProductTraceByKey(@PathVariable("partitionId") String partitionId, @PathVariable("externalKey") String externalKey) {
        Key productKey = getExternalKey(partitionId, externalKey);
        if (productKey == null)
            throw new NotFoundException("The external product key not found with partitionId: " + partitionId + ", externalKey: " + externalKey);

        return getProductTraceByKey(productKey.getPrimaryKey());
    }

    @RequestMapping(value = "/key", method = RequestMethod.POST)
    public void saveDynamoDB(@RequestBody ProductTrace trace) {
        if (trace.getCreatedDateTime() == null)
            trace.setCreatedDateTime(DateTime.now());
        service.save(trace);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductTrace saveMySQL(@RequestBody ProductTrace trace) {
        if (trace.getCreatedDateTime() == null)
            trace.setCreatedDateTime(DateTime.now());
        trace.setProductCount(1);
        return service.saveInMySql(trace);
    }

    @RequestMapping(value = "/external/{partitionId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductTrace saveExternalKey(@PathVariable("partitionId") String partitionId, @RequestBody ProductTrace trace) {
        if (trace == null)
            throw new BadRequestException("product trace body could not be null");

        Key productKey = getExternalKey(partitionId, trace.getProductKey());
        if (productKey == null)
            throw new NotFoundException("The external product key not found with partitionId: " + partitionId + ", externalKey: " + trace.getProductKey());

        trace.setProductKey(productKey.getPrimaryKey());

        return saveMySQL(trace);
    }

    @RequestMapping(value = "/keys", method = RequestMethod.POST)
    public void saveToDynamo(@RequestBody List<ProductTrace> traceList) {
        //save in dynamo
        service.batchSave(traceList);

        traceList.forEach(trace -> {
            Set<String> productKeySet = packageService.getAllChildProductKeySetByKey(trace.getProductKey());
            trace.setProductCount(productKeySet.size());
        });

        service.batchSaveInMySql(traceList);

    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductTrace> query(@RequestParam(value = "status_code") String status) {

        return service.search(status);

    }


    @RequestMapping(value = "/sum", method = RequestMethod.GET)
    public int sumProductCount(@RequestParam("source_id") String sourceId,
                               @RequestParam("source_type") String sourceType,
                               @RequestParam("action") String action,
                               @RequestParam(value = "created_datetime_begin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime start,
                               @RequestParam(value = "created_datetime_end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime end) {
        if (start == null)
            start = DateTime.now().withTime(0, 0, 0, 0);

        if (end == null)
            end = DateTime.now().withTime(23, 59, 59, 999);

        return service.getTotalProductCount(sourceId, sourceType, action, start, end);
    }

    private Key getExternalKey(String partitionId, String externalKey) {
        String key = keyService.formatExternalKey(partitionId, externalKey);
        return keyService.get(key);
    }


}
