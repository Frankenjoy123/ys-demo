package com.yunsoo.dataapi.controller;

import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.service.ProductCategoryService;
import com.yunsoo.service.contract.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public ProductCategory getById(@PathVariable(value = "id") Integer id) {
        ProductCategory pc = productCategoryService.getById(id);
        if (pc == null) {
            throw new NotFoundException("product category not found with id: " + id);
        }
        return pc;
    }

//    @RequestMapping(value = "/{productCategoryId}", method = RequestMethod.GET)
//    public @ResponseBody ProductCategoryModel getProductCategory(@RequestParam(value = "productCategoryId", required = true) Integer productCategoryId) {
//        return this.productCategoryService.getById(productCategoryId);
//    }

    @RequestMapping(value = "/list/rootlevel", method = RequestMethod.GET)
    public List<ProductCategory> getRootProductCategories() {
        return productCategoryService.getRootProductCategories();
    }

    private void validateUser(String userId) {
//        this.accountRepository.findByUsername(userId).orElseThrow(
//                () -> new UserNotFoundException(userId));
    }

}
