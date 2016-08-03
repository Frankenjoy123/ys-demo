package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.TestBase;
import com.yunsoo.common.web.health.Health;
import org.junit.Test;

/**
 * Created by min on 8/2/16.
 */
public class HealthControllerTest extends TestBase {

    @Test
    public void testHealth() throws Exception {
        Health health = restClient.get("health", Health.class);
        System.out.println("health details is " + health.getDetails());
    }
}