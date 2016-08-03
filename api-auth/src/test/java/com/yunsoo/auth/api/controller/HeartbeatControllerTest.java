package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.HeartBeatPackage;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by min on 8/2/16.
 */
public class HeartbeatControllerTest extends TestBase {

    @Before
    public void startBeat() {
        restClient.post("heartbeat", null, null);
    }

    @Test
    public void testGetByDeviceId() throws Exception {
//        AuthDetails authDetails = AuthUtils.getAuthDetails();
//        String deviceId = authDetails.getDeviceId();
        HeartBeatPackage hbPackage = restClient.get("heartbeat?device_id={device_id}", HeartBeatPackage.class, "deviceId");
        System.out.println("package app id is " + hbPackage.getAppId());
    }

    @Test
    public void testBeat() throws Exception {
        restClient.post("heartbeat", null, null);
    }
}