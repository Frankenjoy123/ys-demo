package com.yunsoo.dataapi.controller;

import com.yunsoo.service.ProductService;
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
    private final ProductService productService;

    @Autowired
    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/active/{key}", method = RequestMethod.POST)
    public void active(@PathVariable(value = "key") String key) {
        if (key != null && key.length() > 0) {
            productService.active(key);

        }

    }

}
