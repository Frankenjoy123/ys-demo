package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductDomain;
import com.yunsoo.api.dto.basic.Product;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/9
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductDomain productDomain;

    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public Product get(@PathVariable(value = "key") String key) {
        Product product = productDomain.getProductByKey(key);
        if (product == null) {
            throw new NotFoundException("product");
        }
        return product;
    }

    @RequestMapping(value = "/{key}/active", method = RequestMethod.POST)
    public void active(@PathVariable(value = "key") String key) {
        productDomain.activeProduct(key);
    }

//    @RequestMapping(value = "batch/create", method = RequestMethod.POST)
//    public void batchCreateProductForKeyBatch(@Valid @RequestBody ProductBatchRequest request) {
//        //create Product for exists product keys.
//    }

}

