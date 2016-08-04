package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.TestBase;
import com.yunsoo.common.web.health.Health;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by min on 8/2/16.
 */
public class HealthControllerTest extends TestBase {

    @Test
    public void testHealth() throws Exception {
        Health health = restClient.get("health", Health.class);
        assertEquals(health.getStatus(), Health.Status.UP);
    }
}