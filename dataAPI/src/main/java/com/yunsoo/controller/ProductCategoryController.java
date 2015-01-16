package com.yunsoo.controller;

import com.yunsoo.dbmodel.ProductCategoryModel;
import com.yunsoo.service.Impl.ProductCategoryServiceImpl;
import com.yunsoo.service.ProductCategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Zhe on 2015/1/16.
 */
@RestController
public class ProductCategoryController {

    @RequestMapping(value = "/getrootproductcategories", method = RequestMethod.GET)
    public ProductCategoryModel getRootProductCategories(@RequestParam(value = "id", required = true) Integer id) {
        ProductCategoryService productCategoryService = new ProductCategoryServiceImpl();
        return productCategoryService.getById(id);
    }

    @RequestMapping(value = "/getrootproductcategories", method = RequestMethod.GET)
    public List<ProductCategoryModel> getRootProductCategories() {
        ProductCategoryService productCategoryService = new ProductCategoryServiceImpl();
        return productCategoryService.getRootProductCategories();
    }

}
