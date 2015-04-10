package com.yunsoo.dataapi.controller;

import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.service.ProductKeyTypeService;
import com.yunsoo.data.service.service.contract.ProductKeyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public LookupObject getById(@PathVariable(value = "id") Integer id) {
        ProductKeyType productKeyType = productKeyTypeService.getById(id);
        if (productKeyType == null) {
            throw new NotFoundException("ProductKeyType");
        }
        LookupObject object = new LookupObject();
        object.setId(productKeyType.getId());
        object.setCode(productKeyType.getCode());
        object.setName(productKeyType.getName());
        object.setDescription(productKeyType.getDescription());
        object.setActive(productKeyType.isActive());
        return object;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<LookupObject> getAll(@RequestParam(value = "active", required = false) Boolean active) {
        List<ProductKeyType> productKeyTypes = productKeyTypeService.getAll(active);
        return productKeyTypes.stream().map(p -> {
            LookupObject object = new LookupObject();
            object.setId(p.getId());
            object.setCode(p.getCode());
            object.setName(p.getName());
            object.setDescription(p.getDescription());
            object.setActive(p.isActive());
            return object;
        }).collect(Collectors.toList());
    }
}
