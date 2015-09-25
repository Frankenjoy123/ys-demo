package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.ProductKeyBatchEntity;
import com.yunsoo.data.service.repository.ProductKeyBatchRepository;
import com.yunsoo.data.service.service.ProductKeyBatchService;
import com.yunsoo.data.service.service.contract.ProductKeyBatch;
import com.yunsoo.data.service.service.contract.ProductKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductKeyBatchController.class);

    @Autowired
    private ProductKeyBatchService productKeyBatchService;

    @Autowired
    private ProductKeyBatchRepository productKeyBatchRepository;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductKeyBatchObject getBatchById(@PathVariable(value = "id") String id) {
        ProductKeyBatch batch = productKeyBatchService.getById(id);
        if (batch == null) {
            throw new NotFoundException("ProductKeyBatch not found by [id: " + id + "]");
        }
        return toProductKeyBatchObject(batch);
    }

    @RequestMapping(value = "{id}/keys", method = RequestMethod.GET)
    public ProductKeys getProductKeysById(@PathVariable(value = "id") String id) {
        ProductKeys productKeys = productKeyBatchService.getProductKeysByBatchId(id);
        if (productKeys == null) {
            throw new NotFoundException("ProductKeys");
        }
        return productKeys;
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

        if(isPackage != null) {
            entityPage = productKeyBatchRepository.findByProductKeyTypeCodesAndStatusCodeIn(LookupCodes.ProductKeyType.PACKAGE, statusCodeIn, pageable);
        }
        else if (productBaseId == null) {
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
            @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn) {
        Long sum;
        if (statusCodeIn != null && statusCodeIn.size() > 0) {
            sum = productKeyBatchRepository.sumQuantity(orgId, productBaseId, statusCodeIn);
        } else {
            sum = productKeyBatchRepository.sumQuantity(orgId, productBaseId);
        }
        return sum == null ? 0L : sum;
    }

    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    public boolean existsProductKeyBatch(@RequestParam(value = "quantity", required = false) Integer quantity,
                                         @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn) {

        Integer count = productKeyBatchRepository.countByRestQuantityLessThanAndStatusCodeIn(quantity, statusCodeIn);
        return count > 0 ? true : false;
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductKeyBatchObject create(@RequestBody ProductKeyBatchObject batchObj) {
        ProductKeyBatch batch = toProductKeyBatch(batchObj);
        batch.setId(null);

        LOGGER.info("ProductKeyBatch creating started [quantity: {}]", batch.getQuantity());
        ProductKeyBatch newBatch = productKeyBatchService.create(batch);
        LOGGER.info("ProductKeyBatch created [id: {}, quantity: {}]", newBatch.getId(), newBatch.getQuantity());

        return toProductKeyBatchObject(newBatch);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable(value = "id") String id, @RequestBody ProductKeyBatchObject batchObj) {
        ProductKeyBatch batch = productKeyBatchService.getById(id);
        if (batch == null) {
            throw new NotFoundException("ProductKeyBatch not found by [id: " + id + "]");
        }
        batch.setId(id);
        if (batchObj.getProductBaseId() != null) {
            batch.setProductBaseId(batchObj.getProductBaseId());
        }
        if (batchObj.getStatusCode() != null) {
            batch.setStatusCode(batchObj.getStatusCode());
        }
        if(batchObj.getRestQuantity() != null)
            batch.setRestQuantity(batchObj.getRestQuantity());

        productKeyBatchService.patchUpdate(batch);
    }


    private ProductKeyBatchObject toProductKeyBatchObject(ProductKeyBatch batch) {
        if (batch == null) {
            return null;
        }
        ProductKeyBatchObject batchObj = new ProductKeyBatchObject();
        batchObj.setId(batch.getId());
        batchObj.setQuantity(batch.getQuantity());
        batchObj.setStatusCode(batch.getStatusCode());
        batchObj.setOrgId(batch.getOrgId());
        batchObj.setProductBaseId(batch.getProductBaseId());
        batchObj.setCreatedAppId(batch.getCreatedAppId());
        batchObj.setCreatedAccountId(batch.getCreatedAccountId());
        batchObj.setCreatedDateTime(batch.getCreatedDateTime());
        batchObj.setProductKeyTypeCodes(batch.getProductKeyTypeCodes());
        batchObj.setRestQuantity(batch.getRestQuantity());
        return batchObj;
    }

    private ProductKeyBatchObject toProductKeyBatchObject(ProductKeyBatchEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductKeyBatchObject batchObj = new ProductKeyBatchObject();
        batchObj.setId(entity.getId());
        batchObj.setQuantity(entity.getQuantity());
        batchObj.setStatusCode(entity.getStatusCode());
        batchObj.setOrgId(entity.getOrgId());
        batchObj.setProductBaseId(entity.getProductBaseId());
        batchObj.setCreatedAppId(entity.getCreatedAppId());
        batchObj.setCreatedAccountId(entity.getCreatedAccountId());
        batchObj.setCreatedDateTime(entity.getCreatedDateTime());
        batchObj.setRestQuantity(entity.getRestQuantity());
        String codes = entity.getProductKeyTypeCodes();
        if (codes != null) {
            batchObj.setProductKeyTypeCodes(Arrays.asList(StringUtils.delimitedListToStringArray(codes, ",")));
        }
        return batchObj;
    }

    private ProductKeyBatch toProductKeyBatch(ProductKeyBatchObject batchObj) {
        if (batchObj == null) {
            return null;
        }
        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setId(batchObj.getId());
        batch.setQuantity(batchObj.getQuantity());
        batch.setStatusCode(batchObj.getStatusCode());
        batch.setOrgId(batchObj.getOrgId());
        batch.setProductBaseId(batchObj.getProductBaseId());
        batch.setCreatedAppId(batchObj.getCreatedAppId());
        batch.setCreatedAccountId(batchObj.getCreatedAccountId());
        batch.setCreatedDateTime(batchObj.getCreatedDateTime());
        batch.setProductKeyTypeCodes(batchObj.getProductKeyTypeCodes());
        batch.setRestQuantity(batchObj.getRestQuantity());
        return batch;
    }
}
