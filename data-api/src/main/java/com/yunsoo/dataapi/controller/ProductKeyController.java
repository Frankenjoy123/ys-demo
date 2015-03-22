package com.yunsoo.dataapi.controller;

import com.yunsoo.common.data.object.ProductKeyObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.service.ProductKeyService;
import com.yunsoo.service.contract.ProductKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productkey")
public class ProductKeyController {

    @Autowired
    private ProductKeyService productKeyService;


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

    @RequestMapping(value = "{key}/disabled", method = RequestMethod.PUT)
    public void disableKey(@PathVariable(value = "key") String key, @RequestBody Boolean disabled) {
        productKeyService.setDisabled(key, disabled);
    }

}
