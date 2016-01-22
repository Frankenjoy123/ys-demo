package com.yunsoo.web.taobao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yan on 1/22/2016.
 */
@Controller
@RequestMapping(value = "demo")
public class TaobaoDemoController {

    @RequestMapping(value = "details")
    public String Details(){
        return "tbdemo/details";
    }
    @RequestMapping(value = "factory")
    public String Factory(){
        return "tbdemo/factory";
    }
    @RequestMapping(value = "inspection")
    public String Inspection(){
        return "tbdemo/inspection";
    }
    @RequestMapping(value = "introduce")
    public String Introduce(){
        return "tbdemo/introduce";
    }
    @RequestMapping(value = "trace")
    public String Trace(){
        return "tbdemo/trace";
    }
}
