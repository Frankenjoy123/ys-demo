package com.yunsoo.api.controller;

import com.yunsoo.api.domain.LookupDomain;
import com.yunsoo.api.dto.Lookup;
import com.yunsoo.common.data.LookupCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<Lookup> getAllActive() {
        return lookDomain.getLookupListByType(LookupCodes.LookupType.ProductStatus);
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
