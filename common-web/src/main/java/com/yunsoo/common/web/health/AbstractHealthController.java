package com.yunsoo.common.web.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-25
 * Descriptions:
 */
public abstract class AbstractHealthController {

    @Value("${spring.application.name:}")
    private String appName;


    @RequestMapping(value = "")
    public Health health(@RequestParam(value = "path", required = false, defaultValue = "") List<String> path,
                         @RequestParam(value = "debug", required = false, defaultValue = "false") boolean debug) {
        appName = appName.length() > 0 ? appName : this.getClass().getName();
        Health health = new Health(Health.Status.UP);
        health.setName(appName);
        if (!path.contains(appName)) {
            path.add(appName);
            expandHealth(health, path, debug);
        }
        return health;
    }

    protected void expandHealth(Health health, List<String> path, boolean debug) {
    }

}
