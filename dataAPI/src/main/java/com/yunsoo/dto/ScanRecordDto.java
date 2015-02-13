package com.yunsoo.dto;

import com.yunsoo.service.contract.ScanRecord;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Zhe on 2015/2/12.
 */
public class ScanRecordDto {

    private String productKey;
    private int baseProductId; //TBD
    private int clientId;
    private String deviceId;
    private int userId;
    private String detail;
    private String createdDateTime;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public static ScanRecordDto convertFrom(ScanRecord scanRecord) {
        if (scanRecord == null) return null;

        ScanRecordDto scanRecordDto = new ScanRecordDto();
        scanRecordDto.setBaseProductId(scanRecord.getBaseProductId());
        scanRecordDto.setClientId(scanRecord.getClientId());
        scanRecordDto.setDeviceId(scanRecord.getDeviceId());
        scanRecordDto.setUserId(scanRecord.getUserId());
        scanRecordDto.setProductKey(scanRecord.getProductKey());
        scanRecordDto.setDetail(scanRecord.getDetail());
        scanRecordDto.setCreatedDateTime(scanRecord.getCreatedDateTime());
        return scanRecordDto;
    }

    public static List<ScanRecordDto> FromScanRecordList(List<ScanRecord> scanRecordList) {
        if (scanRecordList == null) return null;
        return scanRecordList.stream().map(ScanRecordDto::convertFrom).collect(Collectors.toList());
    }
}
