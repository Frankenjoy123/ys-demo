package com.yunsoo.dataapi.controller;

import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeyBatchRequestObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.service.ProductKeyBatchService;
import com.yunsoo.service.contract.Product;
import com.yunsoo.service.contract.ProductKeyBatch;
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
        ProductKeyBatchObject batchObj = new ProductKeyBatchObject();
        batchObj.setId(batch.getId());
        batchObj.setQuantity(batch.getQuantity());
        batchObj.setStatusId(batch.getStatusId());
        batchObj.setOrganizationId(batch.getOrganizationId());
        batchObj.setCreatedClientId(batch.getCreatedClientId());
        batchObj.setCreatedAccountId(batch.getCreatedAccountId());
        batchObj.setCreatedDateTime(batch.getCreatedDateTime());
        batchObj.setProductKeyTypeIds(batch.getProductKeyTypeIds());
        batchObj.setProductKeysAddress(batch.getProductKeysAddress());
        return batchObj;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductKeyBatchObject> getByFilter(@RequestParam(value = "organizationId") Integer organizationId,
                                                   @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageIndex == null || pageIndex < 0) {
            pageIndex = 0;
        }
        if (pageSize == null || pageSize > 10000) {
            pageSize = 10000;
        }
        return productKeyBatchService.getByFilterPaged(organizationId, pageIndex, pageSize).stream()
                .map(b -> {
                    ProductKeyBatchObject o = new ProductKeyBatchObject();
                    o.setId(b.getId());
                    o.setQuantity(b.getQuantity());
                    o.setStatusId(b.getStatusId());
                    o.setOrganizationId(b.getOrganizationId());
                    o.setCreatedClientId(b.getCreatedClientId());
                    o.setCreatedAccountId(b.getCreatedAccountId());
                    o.setCreatedDateTime(b.getCreatedDateTime());
                    o.setProductKeyTypeIds(b.getProductKeyTypeIds());
                    o.setProductKeysAddress(b.getProductKeysAddress());
                    return o;
                })
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ProductKeyBatchObject batchCreateProductKeys(@RequestBody ProductKeyBatchRequestObject request) {
        ProductKeyBatchObject batchObj = request.getProductKeyBatch();
        ProductObject productDto = request.getProductTemplate();

        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setQuantity(batchObj.getQuantity());
        batch.setStatusId(batchObj.getStatusId());
        batch.setOrganizationId(batchObj.getOrganizationId());
        batch.setCreatedClientId(batchObj.getCreatedClientId());
        batch.setCreatedAccountId(batchObj.getCreatedAccountId());
        batch.setCreatedDateTime(batchObj.getCreatedDateTime());
        batch.setProductKeyTypeIds(batchObj.getProductKeyTypeIds());
        Product product = null;
        if (productDto != null) {
            product = new Product();
            product.setProductBaseId(productDto.getProductBaseId());
            product.setProductStatusId(productDto.getProductStatusId());
            product.setManufacturingDateTime(productDto.getManufacturingDateTime());
        }
        ProductKeyBatch newBatch = productKeyBatchService.create(batch, product);
        ProductKeyBatchObject newBatchObj = new ProductKeyBatchObject();
        newBatchObj.setId(newBatch.getId());
        newBatchObj.setQuantity(newBatch.getQuantity());
        newBatchObj.setStatusId(newBatch.getStatusId());
        newBatchObj.setOrganizationId(newBatch.getOrganizationId());
        newBatchObj.setCreatedClientId(newBatch.getCreatedClientId());
        newBatchObj.setCreatedAccountId(newBatch.getCreatedAccountId());
        newBatchObj.setCreatedDateTime(newBatch.getCreatedDateTime());
        newBatchObj.setProductKeyTypeIds(newBatch.getProductKeyTypeIds());
        newBatchObj.setProductKeysAddress(newBatch.getProductKeysAddress());

        return newBatchObj;
    }
}
