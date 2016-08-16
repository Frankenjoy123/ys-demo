package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.HeartBeatPackage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by min on 8/2/16.
 */
public class HeartbeatControllerTest extends TestBase {

    private String deviceId;

    @Before
    public void setDeviceId() {
        deviceId = getHostName();
    }

    private HeartBeatPackage getByDeviceId(String id) {
        return restClient.get("heartbeat?device_id={device_id}", HeartBeatPackage.class, id);
    }

    @Test
    public void testGetByDeviceId_404_notExistedId() {
        HeartBeatPackage hbPackage = getByDeviceId(deviceId + "xx");
        assertEquals(hbPackage.getAppId(), null);
    }

    @Test
    public void testGetByDeviceId_404_emptyId() {
        HeartBeatPackage hbPackage = getByDeviceId("");
        assertEquals(hbPackage.getAppId(), null);
    }

    @Test
    public void testGetByDeviceId_404_idSubString() {
        HeartBeatPackage hbPackage = getByDeviceId(deviceId.substring(0, deviceId.length() - 2));
        assertEquals(hbPackage.getAppId(), null);
    }

    @Test
    public void testBeat() throws Exception {
        HeartBeatPackage hbPackage = getByDeviceId(deviceId);
        assertEquals(hbPackage.getStatus(), Constants.DeviceStatus.OFFLINE);
        assertEquals(hbPackage.getAppId(), null);
        restClient.post("heartbeat", null, null);
        hbPackage = getByDeviceId(deviceId);
        assertEquals(getByDeviceId(deviceId).getStatus(), Constants.DeviceStatus.ONLINE);
        assertEquals(hbPackage.getAppId(), "AuthUnitTest");
    }
}