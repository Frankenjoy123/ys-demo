package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.ProductCategoryObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.service.ProductCategoryService;
import com.yunsoo.data.service.service.contract.ProductCategory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/1/16
 * Descriptions:
 */
@RestController
@RequestMapping("/productcategory")
public class ProductCategoryController {

    @Autowired
    private final ProductCategoryService productCategoryService;

    @Autowired
    ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductCategoryObject getById(@PathVariable(value = "id") Integer id) {
        ProductCategory pc = productCategoryService.getById(id);
        if (pc == null) {
            throw new NotFoundException("product category not found with id: " + id);
        }
        return this.FromProductCategory(pc);
    }

    @RequestMapping(value = "/rootlevel", method = RequestMethod.GET)
    public List<ProductCategoryObject> getRootProductCategories() {
        return this.FromProductCategoryList(productCategoryService.getRootProductCategories());
    }

    private ProductCategoryObject FromProductCategory(ProductCategory productCategory) {
        ProductCategoryObject productCategoryObject = new ProductCategoryObject();
        BeanUtils.copyProperties(productCategory, productCategoryObject);
        return productCategoryObject;
    }

    private ProductCategory ToProductCategory(ProductCategoryObject productCategoryObject) {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(productCategoryObject, productCategory);
        return productCategory;
    }

    private List<ProductCategoryObject> FromProductCategoryList(List<ProductCategory> productCategoryList) {
        if (productCategoryList == null) return null;

        List<ProductCategoryObject> productCategoryObjectList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            productCategoryObjectList.add(this.FromProductCategory(productCategory));
        }
        return productCategoryObjectList;
    }

    private List<ProductCategory> ToProductCategoryList(List<ProductCategoryObject> productCategoryObjectList) {
        if (productCategoryObjectList == null) return null;

        List<ProductCategory> productCategoryList = new ArrayList<>();
        for (ProductCategoryObject productCategoryObject : productCategoryObjectList) {
            productCategoryList.add(this.ToProductCategory(productCategoryObject));
        }
        return productCategoryList;
    }

}
