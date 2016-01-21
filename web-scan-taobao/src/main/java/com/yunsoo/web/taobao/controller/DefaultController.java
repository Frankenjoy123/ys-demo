package com.yunsoo.web.taobao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by:   Lijian
 * Created on:   2016-01-21
 * Descriptions:
 */
@Controller
@RequestMapping("")
public class DefaultController {

    @RequestMapping("{key}")
    public String scan(@PathVariable("key") String key) {

        return "default";
    }

}
