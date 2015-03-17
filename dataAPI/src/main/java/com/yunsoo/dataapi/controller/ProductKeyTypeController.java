package com.yunsoo.dataapi.controller;

import com.yunsoo.common.data.object.ProductKeyTypeObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.service.ProductKeyTypeService;
import com.yunsoo.service.contract.ProductKeyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/3/17
 * Descriptions:
 */
@RestController
@RequestMapping("/productkeytype")
public class ProductKeyTypeController {

    @Autowired
    private ProductKeyTypeService productKeyTypeService;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ProductKeyTypeObject getById(@PathVariable(value = "id") Integer id) {
        ProductKeyType productKeyType = productKeyTypeService.getById(id);
        if (productKeyType == null) {
            throw new NotFoundException("ProductKeyType");
        }
        ProductKeyTypeObject object = new ProductKeyTypeObject();
        object.setId(productKeyType.getId());
        object.setCode(productKeyType.getCode());
        object.setDescription(productKeyType.getDescription());
        object.setActive(productKeyType.isActive());
        return object;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductKeyTypeObject> getAll(@RequestParam(value = "active", required = false) Boolean active) {
        List<ProductKeyType> productKeyTypes = productKeyTypeService.getAllProductKeyTypes(active == null ? false : active);
        return productKeyTypes.stream().map(p -> {
            ProductKeyTypeObject object = new ProductKeyTypeObject();
            object.setId(p.getId());
            object.setCode(p.getCode());
            object.setDescription(p.getDescription());
            object.setActive(p.isActive());
            return object;
        }).collect(Collectors.toList());
    }
}
