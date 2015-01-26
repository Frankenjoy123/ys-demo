package com.yunsoo.controller;

import com.yunsoo.service.contract.ProductCategory;
import com.yunsoo.service.Impl.ProductCategoryServiceImpl;
import com.yunsoo.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/1/16.
 */
@RestController
@RequestMapping("/productcategory")
public class ProductCategoryController {

    //    private final BookmarkRepository bookmarkRepository;
//    private final AccountRepository accountRepository;
    private final ProductCategoryService productCategoryService;

    ProductCategoryController() {
        this.productCategoryService = new ProductCategoryServiceImpl();
    }

    @Autowired
    ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @RequestMapping(value = "/model", method = RequestMethod.GET)
    public ProductCategory getRootProductCategories(@RequestParam(value = "id", required = true) Integer id) {
        return productCategoryService.getById(id);
    }

//    @RequestMapping(value = "/{productCategoryId}", method = RequestMethod.GET)
//    public @ResponseBody ProductCategoryModel getProductCategory(@RequestParam(value = "productCategoryId", required = true) Integer productCategoryId) {
//        return this.productCategoryService.getById(productCategoryId);
//    }

    @RequestMapping(value = "/getrootproductcategories", method = RequestMethod.GET)
    public List<ProductCategory> getRootProductCategories() {
        return productCategoryService.getRootProductCategories();
    }

    private void validateUser(String userId) {
//        this.accountRepository.findByUsername(userId).orElseThrow(
//                () -> new UserNotFoundException(userId));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String userId) {
            super("could not find user '" + userId + "'.");
        }
    }
}
