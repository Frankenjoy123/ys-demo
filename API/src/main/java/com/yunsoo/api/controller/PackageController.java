package com.yunsoo.api.controller;

import com.yunsoo.api.dto.PackageBound;
import com.yunsoo.api.dto.ProductStatus;
import com.yunsoo.api.dto.ScanResult;
import com.yunsoo.api.dto.basic.*;
import com.yunsoo.api.dto.basic.Package;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//import org.joda.time.DateTime;

/**
 * Created by Qiyong Yu on 2015/3/8.
 */
@RestController
@RequestMapping("/package")
public class PackageController {

    private RestClient dataAPIClient;
    @Autowired
    PackageController(RestClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }


    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public com.yunsoo.api.dto.basic.Package getDetailByKey(@PathVariable(value = "key") String key) {

        Package productPackage;
        productPackage = dataAPIClient.get("package/{key}", Package.class, key);
        if (productPackage == null) {
            throw new NotFoundException("没有包装信息");
        } else {
            return productPackage;
        }
    }

    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean bind(@RequestBody PackageBound data) throws Exception {
        boolean result = dataAPIClient.post("package/bind", data, Boolean.class);
        return result;
    }

    @RequestMapping(value = "/batch/bind", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean batchBind(@RequestBody PackageBound[] data) throws Exception {
        boolean result = dataAPIClient.post("package/batch/bind", data, Boolean.class);
        return result;
    }

}
