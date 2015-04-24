package com.yunsoo.processor.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2015/4/3
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/")
public class HomeController {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${yunsoo.environment}")
    private String environment;

    @RequestMapping(value = "")
    public String home() {
        return "Processor running on [region: " + region + ", environment: " + environment + "]\n";
    }

}
