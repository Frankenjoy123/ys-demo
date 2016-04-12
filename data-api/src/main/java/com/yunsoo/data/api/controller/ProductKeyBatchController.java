package com.yunsoo.data.api.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeysObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.api.util.ResponseEntityUtils;
import com.yunsoo.data.service.entity.ProductKeyBatchEntity;
import com.yunsoo.data.service.repository.ProductKeyBatchRepository;
import com.yunsoo.data.service.service.ProductKeyBatchService;
import com.yunsoo.data.service.service.contract.ProductKeyBatch;
import com.yunsoo.data.service.service.contract.ProductKeys;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private ProductKeyBatchService productKeyBatchService;

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

    @RequestMapping(value = "{id}/keys", method = RequestMethod.GET)
    public ProductKeysObject getProductKeys(@PathVariable(value = "id") String id) {
        ProductKeys productKeys = productKeyBatchService.getProductKeysByBatchId(id);
        if (productKeys == null) {
            throw new NotFoundException("productKeys not found by [id: " + id + "]");
        }
        return toProductKeysObject(productKeys);
    }

    @RequestMapping(value = "{id}/details", method = RequestMethod.GET)
    public ResponseEntity<?> getProductKeyBatchDetails(@PathVariable(value = "id") String id) throws IOException {
        S3Object s3Object = productKeyBatchService.getProductKeyBatchDetails(id);
        if (s3Object == null) {
            throw new NotFoundException("productKeyBatchDetails not found by [id: " + id + "]");
        }
        return ResponseEntityUtils.convert(s3Object);
    }

    @RequestMapping(value = "{id}/details", method = RequestMethod.PUT)
    public void putProductKeyBatchDetails(@PathVariable(value = "id") String id, HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        long contentLength = request.getContentLengthLong();
        ServletInputStream inputStream = request.getInputStream();
        productKeyBatchService.saveProductKeyBatchDetails(id, inputStream, contentType, contentLength);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductKeyBatchObject> getByFilterPaged(
            @RequestParam(value = "org_id") String orgId,
            @RequestParam(value = "product_base_id", required = false) String productBaseId,
            @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn,
            @RequestParam(value = "is_package", required = false) Boolean isPackage,
            @PageableDefault(size = 1000)
            Pageable pageable,
            HttpServletResponse response) {
        Page<ProductKeyBatchEntity> entityPage;

        if (isPackage != null) {
            if (isPackage)
                entityPage = productKeyBatchRepository.findByOrgIdAndProductKeyTypeCodesAndStatusCodeIn(orgId, LookupCodes.ProductKeyType.PACKAGE, statusCodeIn, pageable);
            else
                entityPage = productKeyBatchRepository.findByOrgIdAndProductKeyTypeCodesNotAndStatusCodeIn(orgId, LookupCodes.ProductKeyType.PACKAGE, statusCodeIn, pageable);
        } else if (productBaseId == null) {
            entityPage = productKeyBatchRepository.findByOrgIdAndStatusCodeIn(orgId, statusCodeIn, pageable);

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
            @RequestParam(value = "marketing_id", required = false) String marketingId) {
        Long sum;
        if (statusCodeIn != null && statusCodeIn.size() > 0) {
            sum = productKeyBatchRepository.sumQuantity(orgId, productBaseId, statusCodeIn);
        } else {
            sum = productKeyBatchRepository.sumQuantity(orgId, productBaseId, marketingId);
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

    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    public boolean existsProductKeyBatch(@RequestParam(value = "quantity", required = false) Integer quantity,
                                         @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn) {

        Integer count = productKeyBatchRepository.countByRestQuantityLessThanAndStatusCodeIn(quantity, statusCodeIn);
        return count > 0;
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductKeyBatchObject create(@RequestBody ProductKeyBatchObject batchObj) {
        ProductKeyBatch batch = toProductKeyBatch(batchObj);
        batch.setId(null);

        log.info(String.format("ProductKeyBatch creating started [quantity: %d]", batch.getQuantity()));
        ProductKeyBatch newBatch = productKeyBatchService.create(batch);
        log.info(String.format("ProductKeyBatch created [id: %s, quantity: %d]", newBatch.getId(), newBatch.getQuantity()));

        return toProductKeyBatchObject(newBatch);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable(value = "id") String id, @RequestBody ProductKeyBatchObject batchObj) {
        ProductKeyBatch batch = toProductKeyBatch(batchObj);
        batch.setId(id);
        productKeyBatchService.patchUpdate(batch);
    }

    @RequestMapping(value = "{id}/marketing_id", method = RequestMethod.PUT)
    public void putMarketingId(@PathVariable(value = "id") String id, @RequestBody String marketingId) {
        ProductKeyBatchEntity entity = productKeyBatchRepository.findOne(id);
        if (id == null) {
            throw new NotFoundException("product key batch not found");
        }
        entity.setMarketingId(marketingId);
        productKeyBatchRepository.save(entity);
    }


    private ProductKeyBatchObject toProductKeyBatchObject(ProductKeyBatch batch) {
        if (batch == null) {
            return null;
        }
        ProductKeyBatchObject batchObj = new ProductKeyBatchObject();
        batchObj.setId(batch.getId());
        batchObj.setBatchNo(batch.getBatchNo());
        batchObj.setQuantity(batch.getQuantity());
        batchObj.setStatusCode(batch.getStatusCode());
        batchObj.setOrgId(batch.getOrgId());
        batchObj.setProductBaseId(batch.getProductBaseId());
        batchObj.setCreatedAppId(batch.getCreatedAppId());
        batchObj.setCreatedAccountId(batch.getCreatedAccountId());
        batchObj.setCreatedDateTime(batch.getCreatedDateTime());
        batchObj.setProductKeyTypeCodes(batch.getProductKeyTypeCodes());
        batchObj.setRestQuantity(batch.getRestQuantity());
        batchObj.setMarketingId(batch.getMarketingId());
        return batchObj;
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
        object.setCreatedAccountId(entity.getCreatedAccountId());
        object.setCreatedDateTime(entity.getCreatedDateTime());
        object.setRestQuantity(entity.getRestQuantity());
        object.setMarketingId(entity.getMarketingId());
        return object;
    }

    private ProductKeyBatch toProductKeyBatch(ProductKeyBatchObject batchObj) {
        if (batchObj == null) {
            return null;
        }
        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setId(batchObj.getId());
        batch.setBatchNo(batchObj.getBatchNo());
        batch.setQuantity(batchObj.getQuantity());
        batch.setStatusCode(batchObj.getStatusCode());
        batch.setOrgId(batchObj.getOrgId());
        batch.setProductBaseId(batchObj.getProductBaseId());
        batch.setCreatedAppId(batchObj.getCreatedAppId());
        batch.setCreatedAccountId(batchObj.getCreatedAccountId());
        batch.setCreatedDateTime(batchObj.getCreatedDateTime());
        batch.setProductKeyTypeCodes(batchObj.getProductKeyTypeCodes());
        batch.setRestQuantity(batchObj.getRestQuantity());
        batch.setMarketingId(batchObj.getMarketingId());
        return batch;
    }

    private ProductKeysObject toProductKeysObject(ProductKeys productKeys) {
        if (productKeys == null) {
            return null;
        }
        ProductKeysObject object = new ProductKeysObject();
        object.setBatchId(productKeys.getBatchId());
        object.setQuantity(productKeys.getQuantity());
        object.setCreatedDateTime(productKeys.getCreatedDateTime());
        object.setProductKeyTypeCodes(productKeys.getProductKeyTypeCodes());
        object.setProductKeys(productKeys.getProductKeys());
        return object;
    }
}
