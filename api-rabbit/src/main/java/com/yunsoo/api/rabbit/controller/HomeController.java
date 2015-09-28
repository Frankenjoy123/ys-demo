package com.yunsoo.api.rabbit.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by:   Zhe
 * Created on:   2015/2/26
 * Descriptions:
 */
@ApiIgnore
@RestController
@RequestMapping(value = "/")
public class HomeController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String home() {
        return "Welcome to YUNSOO API-Rabbit!";
    }

}
