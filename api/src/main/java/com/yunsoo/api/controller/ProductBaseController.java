package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductDomain;
import com.yunsoo.api.dto.basic.ProductBase;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductBase get(@PathVariable(value = "id") Long id) {
        if (id == null || id < 0) {
            throw new BadRequestException("ProductBaseId不应小于0！");
        }
        ProductBase productBase = productDomain.getProductBaseById(id);
        if (productBase == null) {
            throw new NotFoundException("找不到产品");
        }
        return productBase;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductBase> getAllForCurrentOrg() {
        int orgId = 1; //fetch from AuthContext
        return productDomain.getAllProductBaseByOrgId(orgId);
    }

    //create
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ProductBase productBase) {

    }

    //patch update
    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void update(@PathVariable(value = "id") Integer id, @RequestBody ProductBase productBase) throws Exception {
        //patch update, we don't provide functions like update with set null properties.


    }

    //delete
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") Long id) {

    }

}
