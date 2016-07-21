package com.yunsoo.auth.api.controller;

import com.yunsoo.common.web.health.Health;
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

    @RequestMapping(value = "")
    public Health health() {
        return new Health(Health.Status.UP);
    }

}
