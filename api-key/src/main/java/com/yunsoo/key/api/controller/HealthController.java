package com.yunsoo.key.api.controller;

import com.yunsoo.common.web.health.AbstractHealthController;
import com.yunsoo.common.web.health.Health;
import com.yunsoo.key.client.FileApiClient;
import com.yunsoo.key.client.ProcessorClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-15
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/health")
public class HealthController extends AbstractHealthController {

    @Autowired
    private ProcessorClient processorClient;

    @Autowired
    private FileApiClient fileApiClient;

    @Override
    public void expandHealth(Health health, List<String> path, boolean debug) {
        health
                .checkClient(processorClient, path)
                .checkClient(fileApiClient, path);
    }

}
