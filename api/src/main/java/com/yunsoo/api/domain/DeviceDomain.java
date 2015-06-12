package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.DeviceObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/11
 * Descriptions:
 */
@Component
public class DeviceDomain {

    @Autowired
    private RestClient dataAPIClient;

    public DeviceObject getById(String deviceId) {
        try {
            return dataAPIClient.get("device/{id}", DeviceObject.class, deviceId);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public Page<DeviceObject> getByFilterPaged() {
        return null;
    }

    public DeviceObject create(DeviceObject deviceObject) {
        return deviceObject;
    }

    public DeviceObject update(DeviceObject deviceObject) {
        return deviceObject;
    }
}
