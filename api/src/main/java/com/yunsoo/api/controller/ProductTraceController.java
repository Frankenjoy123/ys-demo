package com.yunsoo.api.controller;

import com.yunsoo.api.auth.dto.Organization;
import com.yunsoo.api.auth.service.AuthOrganizationService;
import com.yunsoo.api.domain.ProductBaseDomain;
import com.yunsoo.api.domain.ProductKeyDomain;
import com.yunsoo.api.domain.ProductTraceDomain;
import com.yunsoo.api.dto.ProductBase;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductTrace;
import com.yunsoo.api.dto.ProductTraceDetails;
import com.yunsoo.api.key.dto.Key;
import com.yunsoo.api.key.service.KeyService;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private AuthOrganizationService authOrganizationService;

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private KeyService keyService;

    @RequestMapping(value = "/external/{partitionId}/{externalKey}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'product_trace:read')")
    public ProductTraceDetails getProductTraceByPartitionAndKey(@PathVariable("partitionId") String partitionId, @PathVariable("externalKey") String externalKey) {
        String orgId = AuthUtils.fixOrgId(null);
        Organization organization = authOrganizationService.getById(orgId);
        if(organization == null)
            throw new BadRequestException("current org not existed");

        Key key = keyService.getExternalKey(partitionId, externalKey);
        if(key == null)
            throw new ForbiddenException("the key is not yunsu key");

        ProductKeyBatchObject batchObject = keyDomain.getProductKeyBatchObjectById(key.getBatchId());
        if(batchObject == null)
            throw new BadRequestException("current key batch not existed");

        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(batchObject.getProductBaseId());
        if(productBaseObject == null)
            throw new BadRequestException("current product base not existed");

        ProductTraceDetails details = new ProductTraceDetails();
        details.setOrgName(organization.getName());
        details.setProductName(productBaseObject.getName());
        details.setProductKey(externalKey);
        details.setTraceList(domain.getProductTraceByKey(orgId, partitionId, externalKey));

        return details;
    }

    @RequestMapping(value = "/external/{externalKey}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'product_trace:read')")
    public ProductTraceDetails getProductTraceByKey(@PathVariable("externalKey") String externalKey) {
        String orgId = AuthUtils.fixOrgId(null);
        String partitionId = keyDomain.getKeyBatchPartitionId(orgId);

        return getProductTraceByPartitionAndKey(partitionId, externalKey);
    }

    @RequestMapping(value = "/external/{partitionId}/{externalKey}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#orgId, 'org', 'product_trace:write')")
    public ProductTrace save(@PathVariable("partitionId") String partitionId, @PathVariable("externalKey") String externalKey, @RequestBody ProductTrace trace) {
        if (trace == null)
            throw new BadRequestException("product trace could not be null");

        String orgId = AuthUtils.fixOrgId(null);

        Map<String, String> details = AuthUtils.getCurrentAccount().getDetails();

        trace.setProductKey(externalKey);
        trace.setOrgId(orgId);
        if (details.containsKey("source"))
            trace.setCreatedSourceId(details.get("source"));
        else
            trace.setCreatedSourceId(orgId);

        if (details.containsKey("source_type_code"))
            trace.setCreatedSourceType(details.get("source_type_code"));
        else
            trace.setCreatedSourceType(LookupCodes.TraceSourceType.ORGANIZATION);

        return domain.save(partitionId, trace);
    }

    @RequestMapping(value = "/external/{externalKey}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#orgId, 'org', 'product_trace:write')")
    public ProductTrace save(@PathVariable("externalKey") String externalKey, @RequestBody ProductTrace trace) {
        if (trace == null)
            throw new BadRequestException("product trace could not be null");

        String orgId = AuthUtils.fixOrgId(null);
        String partitionId = keyDomain.getKeyBatchPartitionId(orgId);
        return save(partitionId, externalKey, trace);
    }

    @RequestMapping(value = "/sum", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'product_trace:read')")
    public int sumProductCount(@RequestParam(value = "action", required = false) String action,
                               @RequestParam(value = "created_datetime_begin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime start,
                               @RequestParam(value = "created_datetime_end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime end) {
        Map<String, String> details = AuthUtils.getCurrentAccount().getDetails();
        String sourceId = details.get("source");
        String sourceType = details.get("source_type_code");
        return domain.getSum(sourceType, sourceId, action, start, end);
    }

}
