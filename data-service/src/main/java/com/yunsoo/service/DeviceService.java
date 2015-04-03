package com.yunsoo.service;

import com.yunsoo.service.contract.Device;

/**
 * Created by Jerry on 3/13/2015.
 */
public interface DeviceService {
    public Device get(long id);

    public long save(Device device);

    public ServiceOperationStatus update(Device device);

    public boolean delete(int id, int deleteStatus);
}
