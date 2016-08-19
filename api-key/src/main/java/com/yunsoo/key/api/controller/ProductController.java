package com.yunsoo.key.api.controller;

import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.key.dto.Product;
import com.yunsoo.key.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public Product getByKey(@PathVariable(value = "key") String key) {
        Product product = productService.getByKey(key);
        if (product == null) {
            throw new NotFoundException("product not found");
        }
        return product;
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable(value = "key") String key, @RequestBody Product productRequest) {
        Product product = new Product();
        product.setKey(key);
        product.setStatusCode(productRequest.getStatusCode());
        product.setManufacturingDateTime(productRequest.getManufacturingDateTime());
        product.setDetails(productRequest.getDetails());
        productService.patchUpdate(product);
    }

}
