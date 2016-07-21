package com.yunsoo.api.controller;

import com.yunsoo.api.client.AuthApiClient;
import com.yunsoo.api.client.DataAPIClient;
import com.yunsoo.api.client.ProcessorClient;
import com.yunsoo.common.web.health.Health;
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
    private AuthApiClient authApiClient;

    @Autowired
    private ProcessorClient processorClient;

    @Autowired
    private DataAPIClient dataApiClient;

    @RequestMapping(value = "")
    public Health health() {
        Health authApi = authApiClient.checkHealth();
        Health processor = processorClient.checkHealth();
        Health dataApi = dataApiClient.checkHealth();
        return new Health(Health.Status.UP)
                .mergeStatus(authApi.getStatus())
                .mergeStatus(processor.getStatus())
                .mergeStatus(dataApi.getStatus())
                .withDetail("authApi", authApi)
                .withDetail("processor", processor)
                .withDetail("dataAPI", dataApi);
    }

}