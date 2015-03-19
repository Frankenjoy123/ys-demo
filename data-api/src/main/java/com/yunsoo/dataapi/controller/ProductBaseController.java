package com.yunsoo.dataapi.controller;

import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.dataapi.dto.ProductBaseDto;
import com.yunsoo.service.ProductBaseService;
import com.yunsoo.service.contract.ProductBase;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/3/13.
 */
@RestController
@RequestMapping("/productbase")
public class ProductBaseController {

    @Autowired
    private final ProductBaseService productBaseService;

    @Autowired
    ProductBaseController(ProductBaseService productBaseService) {
        this.productBaseService = productBaseService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ProductBaseDto getById(@PathVariable(value = "id") Long id) {
        ProductBase productBase = productBaseService.getById(id);
        if (productBase == null) {
            throw new NotFoundException("Product");
        }
        ProductBaseDto productBaseDto = new ProductBaseDto();
        BeanUtils.copyProperties(productBase, productBaseDto);
        return productBaseDto;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public List<ProductBase> getMessagesByFilter(@RequestParam(value = "manufacturerId", required = false) Integer manufacturerId,
                                                 @RequestParam(value = "categoryId", required = false) Integer categoryId
    ) {
        return productBaseService.getProductBaseByFilter(manufacturerId, categoryId);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createProductBase(@RequestBody ProductBase productBase) {
        if (productBase == null) throw new IllegalArgumentException("Input parameter ProductBase is invalid!");
        productBaseService.save(productBase);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public void updateProductBase(@RequestBody ProductBase productBase) throws Exception {
        //patch update, we don't provide functions like update with set null properties.
        productBaseService.patchUpdate(productBase);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createProductBase(@PathVariable(value = "id") Long id) {
        productBaseService.delete(id);
    }

}
