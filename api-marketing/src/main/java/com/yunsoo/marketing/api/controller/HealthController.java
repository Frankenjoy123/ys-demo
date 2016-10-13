package com.yunsoo.marketing.api.controller;

import com.yunsoo.common.web.health.AbstractHealthController;
import com.yunsoo.common.web.health.Health;
import com.yunsoo.marketing.client.FileApiClient;
import com.yunsoo.marketing.client.KeyApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-10-13
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/health")
public class HealthController extends AbstractHealthController {

    @Autowired
    private FileApiClient fileApiClient;

    @Autowired
    private KeyApiClient keyApiClient;

    @Override
    public void expandHealth(Health health, List<String> path, boolean debug) {
        health
                .checkClient(fileApiClient, path, debug)
                .checkClient(keyApiClient, path, debug);
    }

}
