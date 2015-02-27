package com.yunsoo.api.controller;

import com.yunsoo.api.dto.ProductStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
@RestController
public class ProductStatusController {
    @RequestMapping(value = "/productstatus/{id}", method = RequestMethod.GET)
    public ProductStatus getById(@PathVariable(value = "id") int id) {
        return ProductStatus.NEW;  //todo
    }
}
