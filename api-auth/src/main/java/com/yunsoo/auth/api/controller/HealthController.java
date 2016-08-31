package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.common.web.exception.UnauthorizedException;
import com.yunsoo.common.web.health.AbstractHealthController;
import com.yunsoo.common.web.health.Health;
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

    @Override
    public void expandHealth(Health health, List<String> path, boolean debug) {
        try {
            health.withDetail("authAccount", AuthUtils.getCurrentAccount());
        } catch (UnauthorizedException ignored) {
        }
    }

}
