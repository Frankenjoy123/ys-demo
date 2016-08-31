package com.yunsoo.key.api.controller;

import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.key.dto.ProductPackage;
import com.yunsoo.key.service.ProductPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
@RestController
@RequestMapping("/productPackage")
public class ProductPackageController {

    @Autowired
    private ProductPackageService productPackageService;


    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public ProductPackage getByKey(@PathVariable(value = "key") String key) {
        ProductPackage productPackage = productPackageService.getByKey(key);
        if (productPackage == null) {
            throw new NotFoundException("productPackage not found by key: " + key);
        }
        return productPackage;
    }

    @RequestMapping(value = "batchSave", method = RequestMethod.POST)
    public int batchSave(@RequestBody List<ProductPackage> packages) {
        return productPackageService.batchSave(packages);
    }

}
