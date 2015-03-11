package com.yunsoo.dataapi.controller;

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
    public ProductDto getByKey(@PathVariable(value = "key") String key) {
        Product product = productService.getByKey(key);
        if (product == null) {
            throw new NotFoundException("Product");
        }
        ProductDto productDto = new ProductDto();
        productDto.setProductKey(product.getProductKey());
        productDto.setProductBaseId(product.getProductBaseId());
        productDto.setProductStatusId(product.getProductStatusId());
        productDto.setManufacturingDateTime((product.getManufacturingDateTime()));
        productDto.setCreatedDateTime(product.getCreatedDateTime());
        return productDto;
    }

    @RequestMapping(value = "/batchcreate/{productbaseid}", method = RequestMethod.POST)
    public int create(@PathVariable(value = "productbaseid") int productBaseId, @RequestBody List<String> productKeyList) {
        if (productBaseId > 0 || productKeyList == null || productKeyList.isEmpty()) {
            throw new BadRequestException();
        }
        //productService.batchCreate(productBaseId, productKeyList);

        return productKeyList.size();
    }

    @RequestMapping(value = "/active/{key}", method = RequestMethod.POST)
    public void active(@PathVariable(value = "key") String key) {
        if (key != null && key.length() > 0) {
            //productService.update(key);

        }

    }

}
