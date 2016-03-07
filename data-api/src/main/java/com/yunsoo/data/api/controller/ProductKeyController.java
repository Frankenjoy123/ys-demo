package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductKeyBatchDetailedObject;
import com.yunsoo.common.data.object.ProductKeyObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.service.ProductKeyService;
import com.yunsoo.data.service.service.contract.Product;
import com.yunsoo.data.service.service.contract.ProductKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/productkey")
public class ProductKeyController {

    @Autowired
    private ProductKeyService productKeyService;


    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public ProductKeyObject getKey(@PathVariable(value = "key") String key) {
        ProductKey productKey = productKeyService.get(key);
        if (productKey == null) {
            throw new NotFoundException("ProductKey " + key);
        }
        return toProductKeyObject(productKey);
    }

    @RequestMapping(value = "batch", method = RequestMethod.PUT)
    public void batchSave(@RequestBody ProductKeyBatchDetailedObject batch) {
        ProductObject productObject = batch.getProductTemplate();
        Product productTemplate = null;
        if (productObject != null) {
            productTemplate = new Product();
            productTemplate.setProductBaseId(productObject.getProductBaseId());
            productTemplate.setProductStatusCode(productObject.getProductStatusCode());
            productTemplate.setManufacturingDateTime(productObject.getManufacturingDateTime());
        }
        productKeyService.batchSave(batch.getId(), batch.getProductKeyTypeCodes(), batch.getProductKeys(), productTemplate);
    }

    @RequestMapping(value = "{key}/disabled", method = RequestMethod.PUT)
    public void disableKey(@PathVariable(value = "key") String key, @RequestBody Boolean disabled) {
        ProductKey productKey = productKeyService.get(key);
        if (productKey == null) {
            throw new NotFoundException("ProductKey " + key);
        }
        productKeyService.setDisabled(key, disabled);
    }

    private ProductKeyObject toProductKeyObject(ProductKey productKey) {
        ProductKeyObject productKeyObj = new ProductKeyObject();
        productKeyObj.setProductKey(productKey.getProductKey());
        productKeyObj.setProductKeyTypeCode(productKey.getProductKeyTypeCode());
        productKeyObj.setProductKeyDisabled(productKey.isProductKeyDisabled());
        productKeyObj.setPrimary(productKey.isPrimary());
        productKeyObj.setProductKeyBatchId(productKey.getProductKeyBatchId());
        productKeyObj.setPrimaryProductKey(productKey.getPrimaryProductKey());
        productKeyObj.setProductKeySet(productKey.getProductKeySet());
        productKeyObj.setCreatedDateTime(productKey.getCreatedDateTime());
        productKeyObj.setDetails(productKey.getDetails());
        return productKeyObj;
    }

}
