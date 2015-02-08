package com.yunsoo.controller;

import com.yunsoo.dto.PackageBoundDTO;
import com.yunsoo.dto.ResultWrapper;
import com.yunsoo.factory.ResultFactory;
import com.yunsoo.service.MessageService;
import com.yunsoo.service.ProductPackageService;
import com.yunsoo.service.contract.Message;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//import org.apache.http.HttpStatus;

/**
 * Created by Qiyong 2015/2/7.
 */
@RestController
@RequestMapping("/package")
public class PackageController {

    @Autowired
    private final ProductPackageService packageService;

    @Autowired
    PackageController(ProductPackageService packageService) {
        this.packageService = packageService;
    }

    /**
     * bind the products or package relations.
     *
     * @param info
     * @return
     */
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public ResponseEntity bind(@RequestBody PackageBoundDTO info) {
        //TODO: create the bound relation, here we possibly bind products or packages.
        // but we do not bind products and packages at the same time.
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public ResponseEntity list(@PathVariable(value = "key") String key) {
        //TODO: list all data below this key. {"products":"...."} or {"packages",[...]}
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/revokeAll/{key}", method = RequestMethod.DELETE)
    public ResponseEntity revokeAll(@PathVariable(value = "key") String key) {
        //TODO: revoke all the relations inside the key package.
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/revoke/{key}", method = RequestMethod.DELETE)
    public ResponseEntity revoke(@PathVariable(value = "key") String key, @RequestBody List<String> subKeys) {
        //TODO: revoke sub key relations. Nested revoke is not supported.
        return new ResponseEntity(HttpStatus.OK);
    }


}
