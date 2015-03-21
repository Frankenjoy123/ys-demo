package com.yunsoo.dataapi.controller;

import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.service.ProductBaseService;
import com.yunsoo.service.contract.ProductBase;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Zhe
 * Created on:   2015/3/13
 * Descriptions:
 */
@RestController
@RequestMapping("/productbase")
public class ProductBaseController {

    @Autowired
    private ProductBaseService productBaseService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductBaseObject getById(@PathVariable(value = "id") Long id) {
        ProductBase productBase = productBaseService.getById(id);
        if (productBase == null) {
            throw new NotFoundException("Product");
        }
        ProductBaseObject productBaseObject = new ProductBaseObject();
        BeanUtils.copyProperties(productBase, productBaseObject);
        return productBaseObject;
    }

    //query
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductBaseObject> getByFilter(
            @RequestParam(value = "manufacturerId", required = false) Integer manufacturerId,
            @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        return productBaseService.getByFilter(manufacturerId, categoryId, true).stream()
                .map(p -> {
                    ProductBaseObject o = new ProductBaseObject();
                    BeanUtils.copyProperties(p, o);
                    return o;
                }).collect(Collectors.toList());
    }

    //create
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ProductBaseObject productBase) {
        ProductBase p = new ProductBase();
        BeanUtils.copyProperties(productBase, p);
        productBaseService.save(p);
    }

    //patch update, we don't provide functions like update with set null properties.
    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable(value = "id") Long id, @RequestBody ProductBaseObject productBase) {
        productBase.setId(id);
        ProductBase p = new ProductBase();
        BeanUtils.copyProperties(productBase, p);
        productBaseService.patchUpdate(p);
    }

    //delete
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") Long id) {
        productBaseService.deactivate(id);
    }

}
