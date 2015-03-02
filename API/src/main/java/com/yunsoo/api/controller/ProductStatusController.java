package com.yunsoo.api.controller;

import com.yunsoo.api.data.DataAPIClient;
import com.yunsoo.api.dto.ProductStatus;
import com.yunsoo.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    private DataAPIClient dataAPIClient;

    @Autowired
    ProductStatusController(DataAPIClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ProductStatus getById(@PathVariable(value = "id") int id) throws ResourceNotFoundException {
        List<ProductStatus> list = query(id, null);
        ProductStatus productStatus = list == null || list.isEmpty() ? null : list.get(0);
        if (productStatus == null) {
            throw new ResourceNotFoundException("ProductStatus");
        } else {
            return productStatus;
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductStatus> query(@RequestParam(value = "id", required = false) Integer id,
                                     @RequestParam(value = "code", required = false) String code) {
        ProductStatus[] array = dataAPIClient.getRequest("productstatus/all/true", ProductStatus[].class);
        List<ProductStatus> resultList, list = Arrays.asList(array == null ? new ProductStatus[0] : array);
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
