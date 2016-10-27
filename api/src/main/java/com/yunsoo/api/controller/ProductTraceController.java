package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductKeyDomain;
import com.yunsoo.api.domain.ProductTraceDomain;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductTrace;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by yan on 10/14/2016.
 */
@RestController
@RequestMapping("producttrace")
public class ProductTraceController {

    @Autowired
    ProductTraceDomain domain;

    @Autowired
    ProductKeyDomain keyDomain;

    @RequestMapping(value = "/external/{partitionId}/{externalKey}", method = RequestMethod.GET)
    public List<ProductTrace> getProductTraceByPartitionAndKey(@PathVariable("partitionId") String partitionId, @PathVariable("externalKey") String externalKey){
       String orgId = AuthUtils.fixOrgId(null);
       return domain.getProductTraceByKey(orgId, partitionId, externalKey);
    }

    @RequestMapping(value = "/external/{externalKey}", method = RequestMethod.GET)
    public List<ProductTrace> getProductTraceByKey(@PathVariable("externalKey") String externalKey){
        String orgId = AuthUtils.fixOrgId(null);
        String partitionId = keyDomain.getKeyBatchPartitionId(orgId);

        return domain.getProductTraceByKey(orgId, partitionId, externalKey);
    }

    @RequestMapping(value = "/external/{partitionId}/{externalKey}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductTrace save(@PathVariable("partitionId") String partitionId,@PathVariable("externalKey") String externalKey, @RequestBody ProductTrace trace){
        if(trace == null)
            throw new BadRequestException("product trace could not be null");

        String orgId = AuthUtils.fixOrgId(null);

        Map<String, String> details = AuthUtils.getCurrentAccount().getDetails();

        trace.setProductKey(externalKey);
        trace.setOrgId(orgId);
        if(details.containsKey("source"))
            trace.setCreatedSourceId(details.get("source"));
        else
            trace.setCreatedSourceId(orgId);

        if(details.containsKey("source_type_code"))
            trace.setCreatedSourceId(details.get("source_type_code"));
        else
            trace.setCreatedSourceType(LookupCodes.TraceSourceType.ORGANIZATION);

        return domain.save(partitionId, trace);
    }

    @RequestMapping(value = "/external/{externalKey}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductTrace save(@PathVariable("externalKey") String externalKey, @RequestBody ProductTrace trace){
        if(trace == null)
            throw new BadRequestException("product trace could not be null");

        String orgId = AuthUtils.fixOrgId(null);
        String partitionId = keyDomain.getKeyBatchPartitionId(orgId);
        return save(partitionId, externalKey, trace);
    }

    @RequestMapping(value = "/sum", method = RequestMethod.GET)
    public int sumProductCount(@RequestParam("source_id") String sourceId,
                               @RequestParam(value = "source_type", required = false) String sourceType,
                               @RequestParam(value = "action", required = false) String action,
                               @RequestParam(value = "created_datetime_begin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)DateTime start,
                               @RequestParam(value = "created_datetime_end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)DateTime end){

        return domain.getSum(sourceType, sourceId, action, start, end);
    }

}
