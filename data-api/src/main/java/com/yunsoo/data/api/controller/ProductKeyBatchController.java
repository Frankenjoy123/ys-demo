package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.ProductKeyBatchEntity;
import com.yunsoo.data.service.repository.ProductKeyBatchRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by:   Lijian
 * Created on:   2015/3/22
 * Descriptions:
 */

@RestController
@RequestMapping("/productkeybatch")
public class ProductKeyBatchController {

    @Autowired
    private ProductKeyBatchRepository productKeyBatchRepository;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductKeyBatchObject getById(@PathVariable(value = "id") String id) {
        ProductKeyBatchEntity entity = productKeyBatchRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("productKeyBatch not found by [id: " + id + "]");
        }
        return toProductKeyBatchObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductKeyBatchObject> getByFilterPaged(
            @RequestParam(value = "org_id") String orgId,
            @RequestParam(value = "org_ids", required = false) List<String> orgIdIn,
            @RequestParam(value = "product_base_id", required = false) String productBaseId,
            @RequestParam(value = "create_account", required = false) String createAccount,
            @RequestParam(value = "device_id", required = false) String deviceId,
            @RequestParam(value = "create_datetime_start", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeStart,
            @RequestParam(value = "create_datetime_end", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate createdDateTimeEnd,
            @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn,
            @RequestParam(value = "is_package", required = false) Boolean isPackage,   //包装码
            @PageableDefault(size = 1000)
            Pageable pageable,
            HttpServletResponse response) {
        Page<ProductKeyBatchEntity> entityPage;

        if (isPackage != null) {
            if (isPackage)
                entityPage = productKeyBatchRepository.findByOrgIdAndProductKeyTypeCodesAndStatusCodeInAndCreatedDeviceId(orgId, LookupCodes.ProductKeyType.PACKAGE, statusCodeIn, deviceId, pageable);
            else {

                DateTime createdDateTimeStartTo = null;
                DateTime createdDateTimeEndTo = null;

                if (createdDateTimeStart != null && !StringUtils.isEmpty(createdDateTimeStart.toString()))
                    createdDateTimeStartTo = createdDateTimeStart.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));

                if (createdDateTimeEnd != null && !StringUtils.isEmpty(createdDateTimeEnd.toString()))
                    createdDateTimeEndTo = createdDateTimeEnd.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusDays(1);

                entityPage = productKeyBatchRepository.findByFilter(orgId, productBaseId, createAccount, createdDateTimeStartTo, createdDateTimeEndTo, pageable);
            }
        } else if (productBaseId == null) {
            if(orgIdIn == null){
                orgIdIn = new ArrayList<>();
                orgIdIn.add(orgId);
            }
            entityPage = productKeyBatchRepository.findByOrgIdInAndStatusCodeIn(orgIdIn, statusCodeIn, pageable);

        } else {
            String pId = productBaseId.toLowerCase().equals("null") ? null : productBaseId;
            entityPage = productKeyBatchRepository.findByOrgIdAndProductBaseIdAndStatusCodeIn(orgId, pId, statusCodeIn, pageable);
        }
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }
        return StreamSupport.stream(entityPage.spliterator(), false)
                .map(this::toProductKeyBatchObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "sum/quantity", method = RequestMethod.GET)
    public Long sumQuantity(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "product_base_id", required = false) String productBaseId,
            @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn,
            @RequestParam(value = "marketing_id", required = false) String marketingId,
            @RequestParam(value = "start_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
            @RequestParam(value = "end_time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime) {
        Long sum;
        if (statusCodeIn != null && statusCodeIn.size() > 0) {
            sum = productKeyBatchRepository.sumQuantity(orgId, productBaseId, statusCodeIn);
        } else {
            DateTime startDateTime = null;
            DateTime endDateTime = null;

            if ((startTime != null) && !StringUtils.isEmpty(startTime.toString()))
                startDateTime = startTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8));
            if ((endTime != null) && !StringUtils.isEmpty(endTime.toString()))
                endDateTime = endTime.toDateTimeAtStartOfDay(DateTimeZone.forOffsetHours(8)).plusHours(23).plusMinutes(59).plusSeconds(59).plusMillis(999);

            sum = productKeyBatchRepository.sumQuantityMarketing(orgId, productBaseId, marketingId, startDateTime, endDateTime);
        }
        return sum == null ? 0L : sum;
    }

    @RequestMapping(value = "sum/time", method = RequestMethod.GET)
    public Long sumQuantityPerTime(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "product_base_id", required = false) String productBaseId,
            @RequestParam(value = "start_time", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            DateTime startTime,
            @RequestParam(value = "end_time", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            DateTime endTime) {

        Long sum = productKeyBatchRepository.sumQuantityTime(orgId, productBaseId, startTime, endTime);

        return sum == null ? 0L : sum;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable(value = "id") String id, @RequestBody ProductKeyBatchObject batchObj) {
        ProductKeyBatchEntity entity = productKeyBatchRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("productKeyBatch not found by id: " + id);
        }
        if (batchObj.getProductBaseId() != null) {
            entity.setProductBaseId(batchObj.getProductBaseId());
        }
        if (batchObj.getStatusCode() != null) {
            entity.setStatusCode(batchObj.getStatusCode());
        }
        if (batchObj.getMarketingId() != null) {
            entity.setMarketingId(batchObj.getMarketingId());
        }
        if (batchObj.getDownloadNo() != 0) {
            entity.setDownloadNo(batchObj.getDownloadNo());
        }

        productKeyBatchRepository.save(entity);
    }

    @RequestMapping(value = "{id}/marketing_id", method = RequestMethod.PUT)
    public void putMarketingId(@PathVariable(value = "id") String id, @RequestBody(required = false) String marketingId) {
        ProductKeyBatchEntity entity = productKeyBatchRepository.findOne(id);
        if (id == null) {
            throw new NotFoundException("product key batch not found");
        }
        if (marketingId != null) {
            entity.setMarketingId(marketingId);
        } else {
            entity.setMarketingId(null);
        }
        productKeyBatchRepository.save(entity);
    }

    @RequestMapping(value = "/marketing/{id}", method = RequestMethod.GET)
    public List<ProductKeyBatchObject> getKeyBatchByMarketingId(@PathVariable(value = "id") String marketingId) {

        List<ProductKeyBatchEntity> productKeyBatchEntities = productKeyBatchRepository.findByMarketingId(marketingId);

        return productKeyBatchEntities.stream()
                .map(this::toProductKeyBatchObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/partition", method = RequestMethod.GET)
    public List<String> getPartitionIdByOrgId(@RequestParam("org_id") String orgId){
        return productKeyBatchRepository.findDistinctPartitionIdByOrgIdOrderByCreatedDateTimeDesc(orgId);
    }

    private ProductKeyBatchObject toProductKeyBatchObject(ProductKeyBatchEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductKeyBatchObject object = new ProductKeyBatchObject();
        object.setId(entity.getId());
        object.setBatchNo(entity.getBatchNo());
        object.setQuantity(entity.getQuantity());
        object.setStatusCode(entity.getStatusCode());
        if (entity.getProductKeyTypeCodes() != null) {
            object.setProductKeyTypeCodes(Arrays.asList(StringUtils.commaDelimitedListToStringArray(entity.getProductKeyTypeCodes())));
        }
        object.setProductBaseId(entity.getProductBaseId());
        object.setOrgId(entity.getOrgId());
        object.setCreatedAppId(entity.getCreatedAppId());
        object.setCreatedDeviceId(entity.getCreatedDeviceId());
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setMarketingId(entity.getMarketingId());
        object.setDownloadNo(entity.getDownloadNo());
        return object;
    }

}
