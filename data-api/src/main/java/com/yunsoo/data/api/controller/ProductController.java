package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.service.ProductService;
import com.yunsoo.data.service.service.contract.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by:   Lijian
 * Created on:   2015/2/16
 * Descriptions:
 */

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public ProductObject getByKey(@PathVariable(value = "key") String key) {
        Product product = productService.getByKey(key);
        if (product == null) {
            throw new NotFoundException("product key not found");
        }
        return toProductObject(product);
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable(value = "key") String key, @RequestBody ProductObject productObject) {
        Product product = new Product();
        product.setProductKey(key);
        product.setProductStatusCode(productObject.getProductStatusCode());
        product.setManufacturingDateTime(productObject.getManufacturingDateTime());
        product.setDetails(productObject.getDetails());
        productService.patchUpdate(product);
    }


    private ProductObject toProductObject(Product product) {
        ProductObject object = new ProductObject();
        object.setProductKey(product.getProductKey());
        object.setProductKeyTypeCode(product.getProductKeyTypeCode());
        object.setProductKeyBatchId(product.getProductKeyBatchId());
        object.setProductKeySet(product.getProductKeySet());
        object.setCreatedDateTime(product.getCreatedDateTime());
        object.setProductBaseId(product.getProductBaseId());
        object.setProductStatusCode(product.getProductStatusCode());
        object.setManufacturingDateTime(product.getManufacturingDateTime());
        object.setDetails(product.getDetails());
        return object;
    }

}
