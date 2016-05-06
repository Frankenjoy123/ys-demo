package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductKeyBatchCreateObject;
import com.yunsoo.common.data.object.ProductKeyObject;
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

    @RequestMapping(value = "{key}/disabled", method = RequestMethod.PUT)
    public void disableKey(@PathVariable(value = "key") String key, @RequestBody Boolean disabled) {
        productKeyService.setDisabled(key, disabled);
    }

    @RequestMapping(value = "batch", method = RequestMethod.PUT)
    public void batchSave(@RequestBody ProductKeyBatchCreateObject object) {
        Product productTemplate = new Product();
        productTemplate.setProductBaseId(object.getProductBaseId());
        productTemplate.setProductStatusCode(object.getProductStatusCode());
        productTemplate.setCreatedDateTime(object.getCreatedDateTime());
        productTemplate.setManufacturingDateTime(object.getManufacturingDateTime());

        productKeyService.batchSave(object.getProductKeyBatchId(), object.getProductKeyTypeCodes(), object.getProductKeys(), productTemplate);
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
        return productKeyObj;
    }

}
