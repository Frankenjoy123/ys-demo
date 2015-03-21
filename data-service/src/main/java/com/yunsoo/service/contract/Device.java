package com.yunsoo.service.contract;


import com.yunsoo.dbmodel.DeviceModel;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by KB
 * Updated by Jerry
 */
public class Device {
    private long id;
    private Integer status;
    private String deviceName;
    private String deviceOS;
    private String deviceGUID;
    private String deviceCertificate;
    private Integer pointId;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public String getDeviceOS() { return deviceOS; }
    public void setDeviceOS(String deviceOS) { this.deviceOS = deviceOS; }

    public String getDeviceGUID() { return deviceGUID; }
    public void setDeviceGUID(String deviceGUID) { this.deviceGUID = deviceGUID; }

    public String getDeviceCertificate() { return deviceCertificate; }
    public void setDeviceCertificate(String deviceCertificate) { this.deviceCertificate = deviceCertificate;}

    public Integer getPointId() { return pointId; }
    public void setPointId(Integer pointId) { this.pointId = pointId; }

    public static Device FromModel(DeviceModel model) {
        if (model == null) return null;
        Device device = new Device();
        device.setId(model.getId());
        device.setStatus(model.getStatus());
        device.setDeviceName(model.getDeviceName());
        device.setDeviceOS(model.getDeviceOS());
        device.setDeviceGUID(model.getDeviceGUID());
        device.setDeviceCertificate(model.getDeviceCertificate());
        device.setPointId(model.getPointId());

        return device;
    }

    public static DeviceModel ToModel(Device device) {
        if (device == null) return null;
        DeviceModel model = new DeviceModel();
        if (device.getId() >= 0) {
            model.setId(device.getId());
        }
        model.setStatus(device.getStatus());
        model.setDeviceName(device.getDeviceName());
        model.setDeviceOS(device.getDeviceOS());
        model.setDeviceGUID(device.getDeviceGUID());
        model.setDeviceCertificate(device.getDeviceCertificate());
        model.setPointId(device.getPointId());

        return model;
    }
}
