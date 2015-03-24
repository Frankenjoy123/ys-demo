package com.yunsoo.api.controller;

import com.yunsoo.api.domain.LookupDomain;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.api.dto.ProductStatus;
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

    @Autowired
    private LookupDomain lookDomain;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductStatus> getAllActive() {
        return lookDomain.getAllProductStatuses(true);
    }

//    @RequestMapping(value = "search", method = RequestMethod.GET)
//    public List<ProductStatus> search(@RequestParam(value = "id", required = false) Integer id,
//                                     @RequestParam(value = "code", required = false) String code) {
//        Map<String, String> params = new HashMap<>();
//        params.put("active", Boolean.toString(true));
//        ProductStatus[] array = dataAPIClient.get("productstatus", ProductStatus[].class, params);
//        List<ProductStatus> resultList, list = Arrays.asList(array == null ? new ProductStatus[0] : array);
//        if (id != null) {
//            //query by id
//            resultList = list.stream().filter(i -> i.getId() == id).collect(Collectors.toList());
//        } else if (code != null) {
//            //query by code
//            resultList = list.stream().filter(i -> code.equals(i.getCode())).collect(Collectors.toList());
//        } else {
//            resultList = list;
//        }
//        return resultList;
//    }
}
