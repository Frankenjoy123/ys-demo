package com.yunsoo.data.api.controller;

/**
 * Created by Zhe on 2015/1/20.
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles requests for the application home page.
 */
@RestController
public class HomeController {

    @RequestMapping(value = "/")
    public String home() {
        System.out.println("HomeController: Passing through...");
        return "Welcome to YUNSOO Data API!";
    }
}
