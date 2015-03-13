package com.yunsoo.dao;

import com.yunsoo.dbmodel.DeviceModel;

/**
 * Created by Jerry on 3/13/2015.
 */
public interface DeviceDao {
    public DeviceModel get(long id);

    public long save(DeviceModel deviceModel);

    public DaoStatus update(DeviceModel deviceModel);

    public DaoStatus delete(long id, int deleteStatus);
}
