package com.yunsoo.api.dto.basic;

/**
 * Created by Zhe on 2015/3/15.
 */
public class LogisticsCheckPath {
    private long Id;
    private String productKey;
    private Integer status_id;
    private Integer startCheckPoint;
    private Integer endCheckPoint;
    private String startDate;
    private String desc;
    private String endDate;
    private Long operator;
    private String deviceId;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public Integer getStartCheckPoint() {
        return startCheckPoint;
    }

    public void setStartCheckPoint(Integer startCheckPoint) {
        this.startCheckPoint = startCheckPoint;
    }

    public Integer getEndCheckPoint() {
        return endCheckPoint;
    }

    public void setEndCheckPoint(Integer endCheckPoint) {
        this.endCheckPoint = endCheckPoint;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
