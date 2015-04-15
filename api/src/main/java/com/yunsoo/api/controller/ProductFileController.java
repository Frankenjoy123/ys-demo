package com.yunsoo.api.controller;

import com.yunsoo.common.data.object.ProductFileObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jerry on 4/14/2015.
 */
@RestController
@RequestMapping(value = "/productfile")
public class ProductFileController {

    @Autowired
    private RestClient dataAPIClient;

    @RequestMapping(value = "/createby/{createby}/status/{status}/filetype/{filetype}", method = RequestMethod.GET)
    public List<ProductFileObject> get(@PathVariable(value = "createby") Long createby,
                                       @PathVariable(value = "status") Integer status,
                                       @PathVariable(value = "filetype") Integer filetype) {

        ProductFileObject[] objects =
                dataAPIClient.get("productfile/createby/{createby}/status/{status}/filetype/{filetype}",
                        ProductFileObject[].class,
                        createby,
                        status,
                        filetype);

        if (objects == null)
            throw new NotFoundException("Product file not found");

        return Arrays.asList(objects);
    }
}
