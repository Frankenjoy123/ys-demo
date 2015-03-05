package com.yunsoo.api.controller;

import com.yunsoo.api.data.RestClient;
import com.yunsoo.api.dto.ProductStatus;
import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.error.ErrorResultCode;
import com.yunsoo.common.web.error.APIErrorResultCode;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productstatus")
public class ProductStatusController {

    private RestClient dataAPIClient;

    @Autowired
    ProductStatusController(RestClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ProductStatus getById(@PathVariable(value = "id") int id) throws NotFoundException {
        ProductStatus productStatus;
        productStatus = dataAPIClient.get("productstatus/{id}", ProductStatus.class, id);
        if (productStatus == null) {
            throw new NotFoundException("ProductStatus");
        } else {
            return productStatus;
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductStatus> query(@RequestParam(value = "id", required = false) Integer id,
                                     @RequestParam(value = "code", required = false) String code) {
        Map<String, String> params = new HashMap<>();
        params.put("active", Boolean.toString(true));
        ProductStatus[] array = dataAPIClient.get("productstatus", ProductStatus[].class, params);
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
