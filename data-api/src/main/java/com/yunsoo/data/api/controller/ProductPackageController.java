package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductPackageObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.service.ProductPackageService;
import com.yunsoo.data.service.service.contract.ProductPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-06-23
 * Descriptions:
 */
@RestController
@RequestMapping("/productpackage")
public class ProductPackageController {

    @Autowired
    private ProductPackageService productPackageService;


    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public ProductPackageObject getByKey(@PathVariable(value = "key") String key) {
        ProductPackage productPackage = productPackageService.getByKey(key);
        if (productPackage == null) {
            throw new NotFoundException("productPackage not found by key: " + key);
        }
        return productPackage.toProductPackageObject();
    }

    @RequestMapping(value = "batch", method = RequestMethod.POST)
    public void batchPackage(@RequestBody List<ProductPackageObject> packages) {
        List<ProductPackage> productPackages = packages.stream().map(ProductPackage::new).collect(Collectors.toList());
        productPackageService.batchPackage(productPackages);
    }

}
