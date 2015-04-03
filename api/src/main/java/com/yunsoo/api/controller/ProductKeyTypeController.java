package com.yunsoo.api.controller;

import com.yunsoo.api.domain.LookupDomain;
import com.yunsoo.api.dto.ProductKeyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/19
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productkeytype")
public class ProductKeyTypeController {

    @Autowired
    private LookupDomain lookDomain;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductKeyType> getAllActive() {
        return lookDomain.getAllProductKeyTypes(true);
    }

}
