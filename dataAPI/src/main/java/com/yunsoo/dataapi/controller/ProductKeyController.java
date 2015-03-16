package com.yunsoo.dataapi.controller;

import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeyBatchRequestObject;
import com.yunsoo.common.data.object.ProductKeyObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.service.ProductKeyBatchService;
import com.yunsoo.service.ProductKeyService;
import com.yunsoo.service.contract.Product;
import com.yunsoo.service.contract.ProductKey;
import com.yunsoo.service.contract.ProductKeyBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productkey")
public class ProductKeyController {

    private final ProductKeyService productKeyService;

    private final ProductKeyBatchService productKeyBatchService;

    @Autowired
    ProductKeyController(ProductKeyService productKeyService,
                         ProductKeyBatchService productKeyBatchService) {
        this.productKeyService = productKeyService;
        this.productKeyBatchService = productKeyBatchService;
    }

    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public ProductKeyObject getKey(@PathVariable(value = "key") String key) {
        ProductKeyObject productKeyObj = new ProductKeyObject();
        ProductKey productKey = productKeyService.get(key);
        if (productKey == null) {
            throw new NotFoundException("ProductKey");
        }
        productKeyObj.setProductKey(productKey.getProductKey());
        productKeyObj.setProductKeyTypeId(productKey.getProductKeyTypeId());
        productKeyObj.setProductKeyDisabled(productKey.isProductKeyDisabled());
        productKeyObj.setPrimary(productKey.isPrimary());
        productKeyObj.setProductKeyBatchId(productKey.getProductKeyBatchId());
        productKeyObj.setPrimaryProductKey(productKey.getPrimaryProductKey());
        productKeyObj.setProductKeySet(productKey.getProductKeySet());
        productKeyObj.setCreatedDateTime(productKey.getCreatedDateTime());
        return productKeyObj;
    }

    @RequestMapping(value = "{key}/disable", method = RequestMethod.PUT)
    public void disableKey(@PathVariable(value = "key") String key) {
        productKeyService.disable(key);
    }


    //batch

    @RequestMapping(value = "batch/{id}", method = RequestMethod.GET)
    public ProductKeyBatchObject getBatchById(@PathVariable(value = "id") String idStr) {
        int idInt;
        try {
            idInt = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            throw new BadRequestException("invalid id");
        }
        ProductKeyBatch batch = productKeyBatchService.getById(idInt);
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


    @RequestMapping(value = "batch/create", method = RequestMethod.POST)
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
        batch.setProductKeysAddress(batchObj.getProductKeysAddress());
        Product product = null;
        if (productDto != null) {
            product = new Product();
            product.setProductBaseId(productDto.getProductBaseId());
            product.setProductStatusId(productDto.getProductStatusId());
            product.setManufacturingDateTime((productDto.getManufacturingDateTime()));
            product.setCreatedDateTime(productDto.getCreatedDateTime());
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
