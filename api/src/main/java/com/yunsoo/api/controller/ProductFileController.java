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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductFileObject> get(@RequestParam(value = "createby", required = true) String createby,
                                       @RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
                                       @RequestParam(value = "filetype", required = false, defaultValue = "0") Integer filetype,
                                       @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        ProductFileObject[] objects =
                dataAPIClient.get("productfile?createby={createby}&&status={status}&&filetype={filetype}&&pageIndex={pageIndex}&&pageSize={pageSize}",
                        ProductFileObject[].class,
                        createby,
                        status,
                        filetype,
                        pageIndex,
                        pageSize);

        if (objects == null)
            throw new NotFoundException("Product file not found");

        return Arrays.asList(objects);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Long getCount(@RequestParam(value = "createby", required = true) String createby,
                         @RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
                         @RequestParam(value = "filetype", required = false, defaultValue = "0") Integer filetype) {

        Long count = 0l;
        count = dataAPIClient.get("productfile/count?createby={createby}&&status={status}&&filetype={filetype}",
                Long.class,
                createby,
                status,
                filetype);

        return count;
    }
}
