package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductCategoryDomain;
import com.yunsoo.api.dto.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/3
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productCategory")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryDomain productCategoryDomain;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductCategory> getByFilter(@RequestParam(value = "active", required = false) Boolean active) {
        return productCategoryDomain.getProductCategories().stream().map(ProductCategory::new).collect(Collectors.toList());
    }
}
