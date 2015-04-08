package com.yunsoo.dataapi.controller;

import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.service.ProductKeyBatchService;
import com.yunsoo.service.contract.ProductKeyBatch;
import com.yunsoo.service.contract.ProductKeys;
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
    public ProductKeyBatchObject getBatchById(@PathVariable(value = "id") String idStr) {
        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException ex) {
            throw new BadRequestException("invalid id");
        }
        ProductKeyBatch batch = productKeyBatchService.getById(id);
        if (batch == null) {
            throw new NotFoundException("product batch");
        }
        return convertToProductKeyBatchObject(batch);
    }

    @RequestMapping(value = "{id}/keys", method = RequestMethod.GET)
    public ProductKeys getProductKeysById(@PathVariable(value = "id") Long id) {
        ProductKeys productKeys = productKeyBatchService.getProductKeysByBatchId(id);
        if (productKeys == null) {
            throw new NotFoundException("ProductKeys");
        }
        return productKeys;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductKeyBatchObject> getByFilter(@RequestParam(value = "organizationId") Integer organizationId,
                                                   @RequestParam(value = "productBaseId") Long productBaseId,
                                                   @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageIndex == null || pageIndex < 0) {
            pageIndex = 0;
        }
        if (pageSize == null || pageSize > 10000) {
            pageSize = 10000;
        }
        return productKeyBatchService.getByFilterPaged(organizationId, productBaseId, pageIndex, pageSize).stream()
                .map(this::convertToProductKeyBatchObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ProductKeyBatchObject create(@RequestBody ProductKeyBatchObject batchObj) {
        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setQuantity(batchObj.getQuantity());
        batch.setStatusId(batchObj.getStatusId());
        batch.setOrganizationId(batchObj.getOrganizationId());
        batch.setProductBaseId(batchObj.getProductBaseId());
        batch.setCreatedClientId(batchObj.getCreatedClientId());
        batch.setCreatedAccountId(batchObj.getCreatedAccountId());
        batch.setCreatedDateTime(batchObj.getCreatedDateTime());
        batch.setProductKeyTypeIds(batchObj.getProductKeyTypeIds());

        ProductKeyBatch newBatch = productKeyBatchService.create(batch);

        return convertToProductKeyBatchObject(newBatch);
    }

    private ProductKeyBatchObject convertToProductKeyBatchObject(ProductKeyBatch batch) {
        ProductKeyBatchObject batchObj = new ProductKeyBatchObject();
        batchObj.setId(batch.getId());
        batchObj.setQuantity(batch.getQuantity());
        batchObj.setStatusId(batch.getStatusId());
        batchObj.setOrganizationId(batch.getOrganizationId());
        batchObj.setProductBaseId(batch.getProductBaseId());
        batchObj.setCreatedClientId(batch.getCreatedClientId());
        batchObj.setCreatedAccountId(batch.getCreatedAccountId());
        batchObj.setCreatedDateTime(batch.getCreatedDateTime());
        batchObj.setProductKeyTypeIds(batch.getProductKeyTypeIds());
        batchObj.setProductKeysAddress(batch.getProductKeysAddress());
        return batchObj;
    }
}
