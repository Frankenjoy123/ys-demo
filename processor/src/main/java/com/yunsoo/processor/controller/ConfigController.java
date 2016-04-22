package com.yunsoo.processor.controller;

import com.yunsoo.processor.config.ProcessorConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
@RequestMapping("/config")
@RestController
public class ConfigController {
    @Autowired
    private ProcessorConfigProperties config;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ProcessorConfigProperties config() {
        return config;
    }
}
