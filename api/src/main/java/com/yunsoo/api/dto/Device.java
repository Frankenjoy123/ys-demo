package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.DeviceObject;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/5/11.
 */
public class Device {

    @JsonProperty("id")
    private String id;

    @JsonProperty("device_name")
    private String deviceName;

    @JsonProperty("device_os")
    private String deviceOs;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("check_point_id")
    private String checkPointId;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("modified_account_id")
    private String modifiedAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("modified_datetime")
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

    public static Device fromDeviceObject(DeviceObject object) {
        if (object == null) return null;
        Device device = new Device();
        BeanUtils.copyProperties(object, device);
        return device;
    }

    public static DeviceObject toDeviceObject(Device device) {
        if (device == null) return null;
        DeviceObject object = new DeviceObject();
        BeanUtils.copyProperties(device, object);
        return object;
    }

    public static List<Device> fromDeviceObjectList(List<DeviceObject> objectList) {
        if (objectList == null) return null;

        List<Device> deviceList = new ArrayList<Device>();
        for (DeviceObject object : objectList) {
            deviceList.add(Device.fromDeviceObject(object));
        }
        return deviceList;
    }

    public static List<DeviceObject> ToDeviceObjectList(Iterable<Device> devices) {
        if (devices == null) return null;

        List<DeviceObject> deviceObjects = new ArrayList<DeviceObject>();
        for (Device device : devices) {
            deviceObjects.add(Device.toDeviceObject(device));
        }
        return deviceObjects;
    }
}
