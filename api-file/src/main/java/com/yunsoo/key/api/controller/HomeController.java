package com.yunsoo.key.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2016-08-15
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/")
public class HomeController {

    @RequestMapping(value = "")
    public String home() {
        return "Welcome to FILE API!";
    }

}