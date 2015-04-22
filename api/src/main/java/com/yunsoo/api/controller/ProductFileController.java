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

    @RequestMapping(value = "/createby/{createby}/status/{status}/filetype/{filetype}/page/{page}", method = RequestMethod.GET)
    public List<ProductFileObject> get(@PathVariable(value = "createby") String createby,
                                       @PathVariable(value = "status") Integer status,
                                       @PathVariable(value = "filetype") Integer filetype,
                                       @PathVariable(value = "page") Integer page) {

        ProductFileObject[] objects =
                dataAPIClient.get("productfile/createby/{createby}/status/{status}/filetype/{filetype}/page/{page}",
                        ProductFileObject[].class,
                        createby,
                        status,
                        filetype,
                        page);

        if (objects == null)
            throw new NotFoundException("Product file not found");

        return Arrays.asList(objects);
    }

    @RequestMapping(value = "/countby/createby/{createby}/status/{status}/filetype/{filetype}", method = RequestMethod.GET)
    public Long getCount(@PathVariable(value = "createby") String createby,
                         @PathVariable(value = "status") Integer status,
                         @PathVariable(value = "filetype") Integer filetype) {

        Long count = 0l;
        count = dataAPIClient.get("productfile/countby/createby/{createby}/status/{status}/filetype/{filetype}",
                Long.class,
                createby,
                status,
                filetype);

        return count;
    }
}
