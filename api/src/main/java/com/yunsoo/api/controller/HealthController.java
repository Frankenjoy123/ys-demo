package com.yunsoo.api.controller;

import com.yunsoo.api.client.*;
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
        health
                .checkClient(authApiClient, path)
                .checkClient(dataApiClient, path)
                .checkClient(processorClient, path)
                .checkClient(fileApiClient, path)
                .checkClient(keyApiClient, path);
    }

}