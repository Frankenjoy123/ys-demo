package com.yunsoo.dataapi.controller;

import com.yunsoo.dataapi.dto.PackageBoundDTO;
import com.yunsoo.service.ProductPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import org.apache.http.HttpStatus;

/**
 * Created by Qiyong 2015/2/7.
 */
@RestController
@RequestMapping("/logistics")
public class LogisticsController {

    @Autowired
    private final ProductPackageService packageService;

    @Autowired
    LogisticsController(ProductPackageService packageService) {
        this.packageService = packageService;
    }

    /**
     * bind the products or package relations.
     *
     * @param info
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseEntity query(@RequestBody PackageBoundDTO info) {
        //TODO: create the bound relation, here we possibly bind products or packages.
        // but we do not bind products and packages at the same time.
        return new ResponseEntity(HttpStatus.OK);
    }    
    

    @RequestMapping(value = "/checkin", method = RequestMethod.GET)
    public ResponseEntity checkIn(@PathVariable(value = "key") String key) {
        //TODO: list all data below this key. {"products":"...."} or {"packages",[...]}
        return new ResponseEntity(HttpStatus.OK);
    }
    
}