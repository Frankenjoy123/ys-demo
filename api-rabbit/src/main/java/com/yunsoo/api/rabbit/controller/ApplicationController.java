package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.ApplicationDomain;
import com.yunsoo.api.rabbit.dto.Application;
import com.yunsoo.common.data.object.ApplicationObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/latest", method = RequestMethod.GET)
    public Application getLatestVersion(@RequestParam(value = "type_code") String typeCode,
                                        @RequestParam(value = "system_version") String systemVersion) {
        ApplicationObject applicationObject = applicationDomain.getLatestApplicationByTypeCode(typeCode, systemVersion);
        if (applicationObject == null) {
            throw new NotFoundException("application not found by [typeCode:" + typeCode + "]");
        }
        return new Application(applicationObject);
    }

}
