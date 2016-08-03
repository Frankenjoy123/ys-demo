package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.TestBase;
import com.yunsoo.auth.dto.Device;
import com.yunsoo.auth.dto.DeviceRegisterRequest;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by min on 8/2/16.
 */
public class DeviceControllerTest extends TestBase {

    String deviceId;

    @Before
    public void registerDevice() {
        if (deviceId == null) {
            DeviceRegisterRequest request = new DeviceRegisterRequest();
            request.setName("TestDevice");
            request.setOs("Android");
            request.setComments("Good device");
            Device device = restClient.post("device/register", request, Device.class);
            deviceId = device.getId();
        }
    }

    private long getDeviceListSize() {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", YUNSU_ORG_ID)
                .build();
        List<Device> list = restClient.get("device"+ query, new ParameterizedTypeReference<List<Device>>() {
        });
        return list.size();
    }

    @Test
    public void testGetById() throws Exception {
        assert getDeviceListSize() == 1;
        Device device = restClient.get("device/{deviceId}", Device.class, deviceId);
        assertEquals(device.getName(), "TestDevice");
    }

    @Test
    public void testGetByFilter() throws Exception {
        assert getDeviceListSize() == 1;
    }

    @Test
    public void testUnregister() throws Exception {
        assert getDeviceListSize() == 1;
        restClient.delete("device/{id}/", deviceId);
        assert getDeviceListSize() == 0;

    }
}