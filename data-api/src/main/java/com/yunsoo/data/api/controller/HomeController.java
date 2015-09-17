package com.yunsoo.data.api.controller;

/**
 * Created by:   Zhe
 * Created on:   2015/1/20
 * Descriptions:
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class HomeController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String home() {
        return "Welcome to YUNSOO Data-API!";
    }

}
