package com.yunsoo.service.Impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.DeviceDao;
import com.yunsoo.dbmodel.DeviceModel;
import com.yunsoo.service.DeviceService;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.contract.Device;
import com.yunsoo.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Jerry on 3/13/2015.
 */
@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceDao deviceDao;

    @Override
    public Device get(long id) {
        return Device.FromModel(deviceDao.get(id));
    }

    @Override
    public long save(Device device) {
        if (device == null ) {
            return -1;
        }
        return deviceDao.save(Device.ToModel(device));
    }

    @Override
    public ServiceOperationStatus update(Device device) {
        if (device == null || device.getId() < 0) {
            return ServiceOperationStatus.InvalidArgument;
        }

        DeviceModel model = new DeviceModel();
        BeanUtils.copyProperties(device, model, SpringBeanUtil.getNullPropertyNames(device));

        DaoStatus daoStatus = deviceDao.update(model);
        if (daoStatus == DaoStatus.success) return ServiceOperationStatus.Success;
        else return ServiceOperationStatus.Fail;
    }

    @Override
    public boolean delete(int id, int deleteStatus) {
        DaoStatus daoStatus = deviceDao.delete(id, deleteStatus);
        if (daoStatus == DaoStatus.success) {
            return true;
        } else {
            return false;
        }
    }
}
