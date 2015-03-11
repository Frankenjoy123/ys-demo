package com.yunsoo.dataapi.controller;


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


    
}
