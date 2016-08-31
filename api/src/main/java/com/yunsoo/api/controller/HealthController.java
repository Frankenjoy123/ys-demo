package com.yunsoo.api.controller;

import com.yunsoo.api.client.*;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import com.yunsoo.common.web.health.AbstractHealthController;
import com.yunsoo.common.web.health.Health;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-07-21
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/health")
public class HealthController extends AbstractHealthController {

    @Autowired
    private AuthApiClient authApiClient;

    @Autowired
    private DataApiClient dataApiClient;

    @Autowired
    private ProcessorClient processorClient;

    @Autowired
    private FileApiClient fileApiClient;

    @Autowired
    private KeyApiClient keyApiClient;


    @Override
    public void expandHealth(Health health, List<String> path, boolean debug) {
        if (debug) {
            try {
                AuthUtils.checkPermission("*", "debug:*");
            } catch (UnauthorizedException | ForbiddenException ignored) {
                debug = false;
            }
        }
        health
                .checkClient(authApiClient, path, debug)
                .checkClient(dataApiClient, path, debug)
                .checkClient(processorClient, path, debug)
                .checkClient(fileApiClient, path, debug)
                .checkClient(keyApiClient, path, debug);
    }

}