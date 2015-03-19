package com.yunsoo.api.controller;

import com.yunsoo.api.biz.ProductDomain;
import com.yunsoo.api.dto.basic.ProductBase;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2015/3/9
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductDomain productDomain;

    @RequestMapping(value = "/active/{key}", method = RequestMethod.POST)
    public void active(@PathVariable(value = "key") String key) {
        if (key != null && key.length() > 0) {
            //productService.active(key);
        }
    }

    @RequestMapping(value = "/base/{baseId}", method = RequestMethod.GET)
    public ProductBase getProductBase(@PathVariable(value = "baseId") Integer baseId) {
        if (baseId == null && baseId < 0) {
            throw new BadRequestException("ProductBaseId不应小于0！");
        }
        return productDomain.getProductBase(baseId);
    }
}

