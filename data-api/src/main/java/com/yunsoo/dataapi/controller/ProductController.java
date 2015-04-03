package com.yunsoo.dataapi.controller;

import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.dataapi.dto.ProductDto;
import com.yunsoo.service.ProductService;
import com.yunsoo.service.contract.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public ProductObject getByKey(@PathVariable(value = "key") String key) {
        Product product = productService.getByKey(key);
        if (product == null) {
            throw new NotFoundException("Product");
        }
        return convertToProductObject(product);
    }

//    @RequestMapping(value = "/batchcreate/{productbaseid}", method = RequestMethod.POST)
//    public int create(@PathVariable(value = "productbaseid") long productBaseId, @RequestBody List<String> productKeyList) {
//        if (productBaseId > 0 || productKeyList == null || productKeyList.isEmpty()) {
//            throw new BadRequestException();
//        }
//        //productService.batchCreate(productBaseId, productKeyList);
//
//        return productKeyList.size();
//    }

    @RequestMapping(value = "/{key}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable(value = "key") String key, @RequestBody ProductObject productObject) {
        Product product = new Product();
        product.setProductKey(key);
        product.setProductStatusId(productObject.getProductStatusId());
        product.setManufacturingDateTime(productObject.getManufacturingDateTime());
        productService.patchUpdate(product);
    }


    private ProductObject convertToProductObject(Product product) {
        ProductObject object = new ProductObject();
        object.setProductKey(product.getProductKey());
        object.setProductBaseId(product.getProductBaseId());
        object.setProductStatusId(product.getProductStatusId());
        object.setManufacturingDateTime((product.getManufacturingDateTime()));
        object.setCreatedDateTime(product.getCreatedDateTime());
        return object;
    }

}
