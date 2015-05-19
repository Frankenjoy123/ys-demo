package com.yunsoo.data.service.service.contract;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.data.service.dbmodel.DeviceModel;
import com.yunsoo.data.service.entity.DeviceEntity;
import com.yunsoo.data.service.entity.UserFollowingEntity;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KB
 * Updated by Jerry and Zhe .  On May 11, 2015
 *
 */
public class Device {
    private String id;
    private String deviceName;
    private String deviceOs;
    private String orgId;
    private String statusCode;
    private String checkPointId;
    private String createdAccountId;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;
    private String modifiedAccountId;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime modifiedDatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCheckPointId() {
        return checkPointId;
    }

    public void setCheckPointId(String checkPointId) {
        this.checkPointId = checkPointId;
    }

    public String getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(String createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getModifiedAccountId() {
        return modifiedAccountId;
    }

    public void setModifiedAccountId(String modifiedAccountId) {
        this.modifiedAccountId = modifiedAccountId;
    }

    public DateTime getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(DateTime modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    public static Device FromEntity(DeviceEntity entity) {
        if (entity == null) return null;

        Device device = new Device();
        BeanUtils.copyProperties(entity, device, new String[]{"createdDateTime"});
        if (entity.getCreatedDateTime() != null) {
            device.setCreatedDateTime(entity.getCreatedDateTime());
        }
        return device;
    }

    public static DeviceEntity ToEntity(Device device) {
        if (device == null) return null;

        DeviceEntity entity = new DeviceEntity();
        BeanUtils.copyProperties(device, entity, new String[]{"createdDateTime"});
        if (device.getCreatedDateTime() != null) {
            entity.setCreatedDateTime(device.getCreatedDateTime());
        }
        return entity;
    }

    public static List<Device> FromEntityList(Iterable<DeviceEntity> entityList) {
        if (entityList == null) return null;
        List<Device> deviceList = new ArrayList<Device>();
        for (DeviceEntity entity : entityList) {
            deviceList.add(Device.FromEntity(entity));
        }
        return deviceList;
    }

    public static List<DeviceEntity> ToEntityList(Iterable<Device> devices) {
        if (devices == null) return null;
        List<DeviceEntity> entityList = new ArrayList<DeviceEntity>();
        for (Device device : devices) {
            entityList.add(Device.ToEntity(device));
        }
        return entityList;
    }
}
