package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.dbmodel.ScanRecordModel;
import org.joda.time.DateTime;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Zhe on 2015/2/4.
 */
public class ScanRecord {
    private long id;
    private String productKey;
    private int baseProductId;
    private int clientId;
    private String deviceId;
    private long userId;
    private String detail;
    private String createdDateTime;
    private Double latitude;
    private Double longitude;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public int getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(int baseProductId) {
        this.baseProductId = baseProductId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDatetime) {
        this.createdDateTime = createdDatetime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public static ScanRecord FromModel(ScanRecordModel scanRecordModel) {
        if (scanRecordModel == null) return null;

        ScanRecord scanRecord = new ScanRecord();
        scanRecord.setId(scanRecordModel.getId());
        scanRecord.setBaseProductId(scanRecordModel.getBaseProductId());
        scanRecord.setClientId(scanRecordModel.getClientId());
        scanRecord.setDeviceId(scanRecordModel.getDeviceId());
        scanRecord.setUserId(scanRecordModel.getUserId());
        scanRecord.setProductKey(scanRecordModel.getProductKey());
        scanRecord.setDetail(scanRecordModel.getDetail());
        scanRecord.setCreatedDateTime(scanRecordModel.getCreatedDateTime().toString());
        scanRecord.setLatitude(scanRecordModel.getLatitude());
        scanRecord.setLongitude(scanRecordModel.getLongitude());
        return scanRecord;
    }

    public static ScanRecordModel ToModel(ScanRecord scanRecord) {
        if (scanRecord == null) return null;

        ScanRecordModel scanRecordModel = new ScanRecordModel();
        scanRecordModel.setId(scanRecord.getId());
        scanRecordModel.setBaseProductId(scanRecord.getBaseProductId());
        scanRecordModel.setClientId(scanRecord.getClientId());
        scanRecordModel.setDeviceId(scanRecord.getDeviceId());
        scanRecordModel.setUserId(scanRecord.getUserId());
        scanRecordModel.setProductKey(scanRecord.getProductKey());
        scanRecordModel.setDetail(scanRecord.getDetail());
        if (scanRecord.getCreatedDateTime() != null) {
            scanRecordModel.setCreatedDateTime(DateTime.parse(scanRecord.getCreatedDateTime()));
        }
        if (scanRecord.getLatitude() != null) {
            scanRecordModel.setLatitude(scanRecord.getLatitude());
        }
        if (scanRecord.getLongitude() != null) {
            scanRecordModel.setLongitude(scanRecord.getLongitude());
        }
        ;
        return scanRecordModel;
    }

    public static List<ScanRecord> FromModelList(List<ScanRecordModel> modelList) {
        if (modelList == null) return null;
        return modelList.stream().map(ScanRecord::FromModel).collect(Collectors.toList());
    }

    public static List<ScanRecordModel> ToModelList(List<ScanRecord> scanRecordList) {
        if (scanRecordList == null) return null;
        return scanRecordList.stream().map(ScanRecord::ToModel).collect(Collectors.toList());
    }
}