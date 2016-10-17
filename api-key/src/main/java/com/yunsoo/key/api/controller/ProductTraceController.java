package com.yunsoo.key.api.controller;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.key.dao.entity.ProductTraceEntity;
import com.yunsoo.key.dao.repository.ProductTraceRepository;
import com.yunsoo.key.dto.Key;
import com.yunsoo.key.dto.ProductTrace;
import com.yunsoo.key.service.KeyService;
import com.yunsoo.key.service.ProductKeyTraceService;
import com.yunsoo.key.service.ProductPackageService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yan on 10/11/2016.
 */
@RestController
@RequestMapping("producttrace")
public class ProductTraceController {

    @Autowired
    ProductKeyTraceService service;

    @Autowired
    ProductPackageService packageService;

    @Autowired
    KeyService keyService;

    @Autowired
    ProductTraceRepository repository;

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public List<ProductTrace> getProductTraceByKey(@PathVariable("key") String key){
        List<String> keyList = packageService.getAllParentKeysByKey(key);
        keyList.add(key);
        return service.getProductTraceListByKeys(keyList);
    }

    @RequestMapping(value = "/external/{partitionId}/{externalKey}", method = RequestMethod.GET)
    public List<ProductTrace> getProductTraceByKey(@PathVariable("partitionId") String partitionId, @PathVariable("externalKey") String externalKey){
        Key productKey = getExternalKey(partitionId, externalKey);
        if(productKey == null)
            throw new NotFoundException("The external product key not found with partitionId: " + partitionId + ", externalKey: " + externalKey);

        return getProductTraceByKey(productKey.getPrimaryKey());
    }

    @RequestMapping(value = "/key", method = RequestMethod.POST)
    public void saveDynamoDB(@RequestBody ProductTrace trace){
        if(trace.getCreatedDateTime() == null)
            trace.setCreatedDateTime(DateTime.now());
        service.save(trace);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductTrace saveMySQL(@RequestBody ProductTrace trace){
        if(trace.getCreatedDateTime() == null)
            trace.setCreatedDateTime(DateTime.now());
        trace.setProductCount(1);
        ProductTraceEntity entity = new ProductTraceEntity(trace);
        entity.setStatusCode(TraceStatus.PENDING.toString());
        repository.save(entity);
        return new ProductTrace(entity);
    }

    @RequestMapping(value = "/external/{partitionId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductTrace saveExternalKey(@PathVariable("partitionId") String partitionId, @RequestBody ProductTrace trace){
        if(trace == null)
            throw new BadRequestException("product trace body could not be null");

        Key productKey = getExternalKey(partitionId, trace.getProductKey());
        if(productKey == null)
            throw new NotFoundException("The external product key not found with partitionId: " + partitionId + ", externalKey: " + trace.getProductKey());

        trace.setProductKey(productKey.getPrimaryKey());

        return  saveMySQL(trace);
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void updateCount(@RequestBody ProductTrace trace){
        ProductTraceEntity entity = repository.findOne(trace.getId());
        if(entity == null)
            throw new NotFoundException("could not find the product trace entity with id: " + trace.getId());
        if(trace.getProductCount() != null) {
            entity.setProductCount(trace.getProductCount());
            entity.setStatusCode(TraceStatus.SYNCHRONIZED.toString());
        }
        repository.save(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductTrace> query(@RequestParam(value = "status_code") String status){
         return repository.findByStatusCode(status).stream().map(ProductTrace::new).collect(Collectors.toList());
    }


    @RequestMapping(value = "/sum", method = RequestMethod.GET)
    public int sumProductCount(@RequestParam("source_id") String sourceId,
                     @RequestParam("source_type") String sourceType,
                     @RequestParam("action") String action,
                     @RequestParam(value = "created_datetime_begin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime start,
                     @RequestParam(value = "created_datetime_end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime end){
        if(start == null)
            start = DateTime.now().withTime(0,0,0,0);

        if(end==null)
            end=DateTime.now().withTime(23,59,59,999);

        return repository.sumProduct(sourceId, sourceType, action, start, end);
    }

    private Key getExternalKey(String partitionId, String externalKey){
        String key = keyService.formatExternalKey(partitionId, externalKey);
        return keyService.get(key);
    }

    private enum TraceStatus {
        PENDING("pending"),
        SYNCHRONIZED("synchronized");

        private String status;

        TraceStatus(String status){
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }

}
