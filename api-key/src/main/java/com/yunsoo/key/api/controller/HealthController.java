package com.yunsoo.key.api.controller;

import com.yunsoo.common.web.health.Health;
import com.yunsoo.key.client.FileApiClient;
import com.yunsoo.key.client.ProcessorClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2016-08-15
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/health")
public class HealthController {

    @Autowired
    private ProcessorClient processorClient;

    @Autowired
    private FileApiClient fileApiClient;

    @RequestMapping(value = "")
    public Health health() {
        Health processor = processorClient.checkHealth();
        Health fileApi = fileApiClient.checkHealth();
        return new Health(Health.Status.UP)
                .withDetail("processor", processor)
                .withDetail("fileApi", fileApi);
    }

}
