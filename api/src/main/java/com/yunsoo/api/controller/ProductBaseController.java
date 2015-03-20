package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductDomain;
import com.yunsoo.api.dto.basic.ProductBase;
import com.yunsoo.common.web.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/20
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productbase")
public class ProductBaseController {

    @Autowired
    private ProductDomain productDomain;

    @RequestMapping(value = "/base/{baseId}", method = RequestMethod.GET)
    public ProductBase getProductBase(@PathVariable(value = "baseId") Integer baseId) {
        if (baseId == null || baseId < 0) {
            throw new BadRequestException("ProductBaseId不应小于0！");
        }
        return productDomain.getProductBase(baseId);
    }

    @RequestMapping(value = "/base", method = RequestMethod.GET)
    public List<ProductBase> getAllProductBasesForCurrentOrg() {
        int orgId = 1; //fetch from AuthContext
        return productDomain.getAllProductBaseByOrgId(orgId);
    }
}
