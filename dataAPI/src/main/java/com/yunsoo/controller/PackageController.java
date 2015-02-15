package com.yunsoo.controller;

import com.yunsoo.dto.PackageBoundDTO;
import com.yunsoo.dto.ResultWrapper;
import com.yunsoo.factory.ResultFactory;
import com.yunsoo.service.MessageService;
import com.yunsoo.service.ProductPackageService;
import com.yunsoo.service.contract.Message;
import com.yunsoo.service.contract.ProductPackage;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
        packageService.bind(info.getPackageKey(), info.getKeys(), info.getOperator());        
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public ResponseEntity<ProductPackage> list(@PathVariable(value = "key") String key) {
        ProductPackage p = packageService.list(key);
        
        return new ResponseEntity<>(p,HttpStatus.OK);
    }

    @RequestMapping(value = "/revokeAll/{key}", method = RequestMethod.POST)
    public ResponseEntity revokeAll(@PathVariable(value = "key") String key) {
        boolean result = packageService.revoke(key);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/revoke/{key}", method = RequestMethod.POST)
    public ResponseEntity revoke(@PathVariable(value = "key") String key, @RequestBody List<String> subKeys) {
        boolean result = packageService.revoke(key, subKeys);
        return new ResponseEntity(HttpStatus.OK);
    }


}
