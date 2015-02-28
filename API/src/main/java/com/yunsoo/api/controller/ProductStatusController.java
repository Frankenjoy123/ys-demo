package com.yunsoo.api.controller;

import com.yunsoo.api.dto.ProductStatus;
import com.yunsoo.api.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productstatus")
public class ProductStatusController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ProductStatus getById(@PathVariable(value = "id") int id) throws ResourceNotFoundException {
        List<ProductStatus> list = new ArrayList<ProductStatus>(Arrays.asList(
                ProductStatus.NEW,
                ProductStatus.ACTIVATED,
                ProductStatus.RECALLED,
                ProductStatus.DELETED));
        //todo
        Optional<ProductStatus> result = list.stream().filter(i -> i.getId() == id).findFirst();
        if (!result.isPresent()) {
            throw new ResourceNotFoundException();
        } else {
            return result.get();
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<ProductStatus> query(@RequestParam(value = "id", required = false) Integer id,
                                     @RequestParam(value = "code", required = false) String code) {
        List<ProductStatus> list = new ArrayList<ProductStatus>(Arrays.asList(
                ProductStatus.NEW,
                ProductStatus.ACTIVATED,
                ProductStatus.RECALLED,
                ProductStatus.DELETED));
        //todo
        List<ProductStatus> resultList = new ArrayList<>();
        if (id != null) {
            //query by id
            resultList = list.stream().filter(i -> i.getId() == id).collect(Collectors.toList());
        } else if (code != null) {
            //query by code
            resultList = list.stream().filter(i -> code.equals(i.getCode())).collect(Collectors.toList());
        } else {
            resultList = list;
        }
        return resultList;
    }
}
