package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.client.AuthApiClient;
import com.yunsoo.api.rabbit.client.DataApiClient;
import com.yunsoo.common.web.health.Health;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2016-08-11
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/health")
public class HealthController {

    @Autowired
    private AuthApiClient authApiClient;

    @Autowired
    private DataApiClient dataApiClient;

    @RequestMapping(value = "")
    public Health health() {
        Health authApi = authApiClient.checkHealth();
        Health dataApi = dataApiClient.checkHealth();
        return new Health(Health.Status.UP)
                .mergeStatus(authApi.getStatus())
                .mergeStatus(dataApi.getStatus())
                .withDetail("authApi", authApi)
                .withDetail("dataApi", dataApi);
    }

}