package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.service.ProductKeyBatchService;
import com.yunsoo.data.service.service.contract.ProductKeyBatch;
import com.yunsoo.data.service.service.contract.ProductKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/3/22
 * Descriptions:
 */

@RestController
@RequestMapping("/productkeybatch")
public class ProductKeyBatchController {

    @Autowired
    private ProductKeyBatchService productKeyBatchService;


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
    public List<ProductKeyBatchObject> getByFilter(@RequestParam(value = "orgId") String orgId,
                                                   @RequestParam(value = "productBaseId") String productBaseId,
                                                   @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageIndex == null || pageIndex < 0) {
            pageIndex = 0;
        }
        if (pageSize == null || pageSize > 1000) {
            pageSize = 1000;
        }
        return productKeyBatchService.getByFilterPaged(orgId, productBaseId, pageIndex, pageSize).stream()
                .map(this::toProductKeyBatchObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ProductKeyBatchObject create(@RequestBody ProductKeyBatchObject batchObj) {
        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setQuantity(batchObj.getQuantity());
        batch.setStatusCode(batchObj.getStatusCode());
        batch.setOrgId(batchObj.getOrgId());
        batch.setProductBaseId(batchObj.getProductBaseId());
        batch.setCreatedAppId(batchObj.getCreatedAppId());
        batch.setCreatedAccountId(batchObj.getCreatedAccountId());
        batch.setCreatedDateTime(batchObj.getCreatedDateTime());
        batch.setProductKeyTypeCodes(batchObj.getProductKeyTypeCodes());

        ProductKeyBatch newBatch = productKeyBatchService.create(batch);

        return toProductKeyBatchObject(newBatch);
    }


    private ProductKeyBatchObject toProductKeyBatchObject(ProductKeyBatch batch) {
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
        batchObj.setProductKeysUri(batch.getProductKeysUri());
        return batchObj;
    }
}
