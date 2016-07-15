package com.yunsoo.auth.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2016-07-13
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/")
public class HomeController {

    @RequestMapping(value = "")
    public String home() {
        return "Welcome to AUTH API!";
    }

}