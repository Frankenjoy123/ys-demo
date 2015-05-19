package com.yunsoo.processor.controller;

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

    @RequestMapping(value = "")
    public String home() {
        return "Processor is running!";
    }

}
