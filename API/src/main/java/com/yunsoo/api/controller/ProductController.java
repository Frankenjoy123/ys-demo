package com.yunsoo.api.controller;

import com.yunsoo.api.dto.ProductBatchRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @RequestMapping(value = "batch/create", method = RequestMethod.POST)
    public void batchCreateProductForKeyBatch(@Valid @RequestBody ProductBatchRequest request) {

    }
}
