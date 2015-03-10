package com.yunsoo.api.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2015/3/9
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @RequestMapping(value = "/active/{key}", method = RequestMethod.POST)
    public void active(@PathVariable(value = "key") String key) {
        if (key != null && key.length() > 0) {
            //productService.active(key);

        }

    }
}
