package com.yunsoo.api.rabbit.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Zhe
 * Created on:   2015/2/26
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/")
public class HomeController {

    @RequestMapping(value = "")
    public String home() {
        return "Welcome to YUNSOO API-Rabbit!";
    }

}
