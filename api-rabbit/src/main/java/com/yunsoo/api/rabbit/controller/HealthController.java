package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.client.*;
import com.yunsoo.common.web.health.AbstractHealthController;
import com.yunsoo.common.web.health.Health;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-11
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
    private FileApiClient fileApiClient;

    @Autowired
    private KeyApiClient keyApiClient;

    @Autowired
    private ThirdApiClient thirdApiClient;

    @Override
    public void expandHealth(Health health, List<String> path, boolean debug) {
        debug = false; //always false
        health
                .checkClient(authApiClient, path, debug)
                .checkClient(dataApiClient, path, debug)
                .checkClient(fileApiClient, path, debug)
                .checkClient(keyApiClient, path, debug)
                .checkClient(thirdApiClient, path, debug);
    }

}