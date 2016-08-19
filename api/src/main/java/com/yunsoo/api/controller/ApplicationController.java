package com.yunsoo.api.controller;

import com.yunsoo.api.config.YSConfigProperties;
import com.yunsoo.api.file.service.FileService;
import com.yunsoo.common.util.ObjectIdGenerator;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2015/9/7
 * Descriptions:
 */
@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private YSConfigProperties YSConfig;

    @Autowired
    private FileService fileService;

    //region config

    @RequestMapping(value = "{appId}/config", method = RequestMethod.GET)
    public ResponseEntity<?> getDefaultConfigByApplicationId(@PathVariable("appId") String appId) {
        if (!ObjectIdGenerator.validate(appId)) {
            throw new BadRequestException("appId not valid");
        }
        String env = YSConfig.getEnvironment();
        ResourceInputStream resourceInputStream = fileService.getFile(String.format("application/%s/config.%s", appId, env));
        if (resourceInputStream == null) {
            resourceInputStream = fileService.getFile(String.format("application/%s/config", appId));
        }
        if (resourceInputStream == null) {
            throw new NotFoundException("config not found");
        }
        return resourceInputStream.toResponseEntity();
    }

    //endregion

}
