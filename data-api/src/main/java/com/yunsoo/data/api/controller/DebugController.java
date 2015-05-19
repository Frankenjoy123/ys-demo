package com.yunsoo.data.api.controller;

import com.yunsoo.common.util.IdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2015/2/28
 * Descriptions: For developer debug use only.
 */
@RestController
@RequestMapping(value = "/debug")
public class DebugController {

    @Value("${yunsoo.debug}")
    private Boolean debug;

    @Value("${yunsoo.environment}")
    private String environment;


    @RequestMapping(value = "")
    public Map<String, Object> info() {
        Map<String, Object> result = new HashMap<>();

        //common info
        result.put("debug", debug);
        result.put("environment", environment);



        return result;
    }
}
