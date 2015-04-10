package com.yunsoo.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Zhe on 2015/2/26.
 */
@RestController
@RequestMapping(value = "/")
public class HomeController {

    @RequestMapping(value = "")
    public String home() {
        System.out.println("HomeController: Passing through...");
        return "Welcome to YUNSOO API-Rabbit!";
    }

    @RequestMapping(value = "home/health")
    public String health() {
        System.out.println("HomeController: Passing through heath check...");
        return "Hi, I am still working!";
    }
}
