package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.ApplicationDomain;
import com.yunsoo.api.rabbit.dto.Application;
import com.yunsoo.common.data.object.ApplicationObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Zhe on 2015/6/15.
 * General client application controller.
 */
@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private ApplicationDomain applicationDomain;


    @RequestMapping(value = "{appId}", method = RequestMethod.GET)
    public Application getApplicationById(@PathVariable("appId") String appId) {
        ApplicationObject applicationObject = applicationDomain.getApplicationById(appId);
        if (applicationObject == null) {
            throw new NotFoundException("application not found by [id:" + appId + "]");
        }
        return new Application(applicationObject);
    }

}
