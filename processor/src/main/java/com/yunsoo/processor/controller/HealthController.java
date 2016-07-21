package com.yunsoo.processor.controller;

import com.yunsoo.common.web.health.Health;
import com.yunsoo.processor.client.DataApiClient1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2016-07-21
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/health")
public class HealthController {

    @Autowired
    private DataApiClient1 dataApiClient;

    @RequestMapping(value = "")
    public Health health() {
        Health dataApi = dataApiClient.checkHealth();
        return new Health(Health.Status.UP)
                .mergeStatus(dataApi.getStatus())
                .withDetail("dataApi", dataApi);
    }

}