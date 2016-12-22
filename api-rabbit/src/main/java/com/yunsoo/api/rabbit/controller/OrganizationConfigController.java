package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.OrganizationConfigDomain;
import com.yunsoo.api.rabbit.domain.ProductBaseDomain;
import com.yunsoo.api.rabbit.key.dto.Product;
import com.yunsoo.api.rabbit.key.service.ProductService;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.util.ObjectIdGenerator;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2016-05-25
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/organization")
public class OrganizationConfigController {

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private OrganizationConfigDomain organizationConfigDomain;

    @Autowired
    private ProductService productService;


    @RequestMapping(value = "config/key/{key}", method = RequestMethod.GET)
    public Map<String, Object> getOrgConfigByKey(@PathVariable("key") String key) {
        Product product = productService.getProductByKey(key);
        if (product == null) {
            throw new NotFoundException("product not found");
        }
        ProductBaseObject productBaseObject = getProductBaseById(product.getProductBaseId());
        return organizationConfigDomain.getConfig(productBaseObject.getOrgId(), false);
    }

    @RequestMapping(value = "config/{id}", method = RequestMethod.GET)
    public Map<String, Object> getOrgConfig(@PathVariable("id") String id) {
        return organizationConfigDomain.getConfig(id, false);
    }

    private ProductBaseObject getProductBaseById(String productBaseId) {
        if (!ObjectIdGenerator.validate(productBaseId)) {
            throw new NotFoundException("product not found");
        }
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
        if (productBaseObject == null) {
            throw new NotFoundException("product not found");
        }
        return productBaseObject;
    }


}
