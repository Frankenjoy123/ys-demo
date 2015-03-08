package com.yunsoo.dataapi.dto;

import com.yunsoo.service.contract.LogisticsCheckPath;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Jerry on 3/6/2015.
 */
public class LogisticsPathsDto {
    private long Id;
    private List<String> productKeys;
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
        this.Id = id;
    }

    public List<String> getProductKey() {
        return productKeys;
    }

    public void setProductKey(List<String> productKeys) {
        this.productKeys = productKeys;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<LogisticsCheckPath> ToLogisticsCheckPath(LogisticsPathsDto pathsDto) {
        if (pathsDto == null) return null;

        List<String> productKeysTemp = pathsDto.getProductKey();
        if(productKeysTemp == null || productKeysTemp.isEmpty()) return null;

        List<LogisticsCheckPath> paths = new ArrayList<LogisticsCheckPath>();
        for (String key : productKeysTemp) {
            LogisticsCheckPath path = new LogisticsCheckPath();
            //Need to get key list from key package
            path.setProductKey(key);

            //Use deviceId to get start check point
            path.setStartCheckPoint(pathsDto.getStartCheckPoint());

            //Record the start date as the server date
            path.setStartDate(DateTime.now());

            path.setEndCheckPoint(pathsDto.getEndCheckPoint());

            if (pathsDto.getEndDate() != null && !pathsDto.getEndDate().isEmpty()) {
                path.setEndDate(DateTime.parse(pathsDto.getEndDate()));
            }
            path.setOperator(pathsDto.getOperator());
            path.setStatus_id(pathsDto.getStatus_id());
            path.setDesc(pathsDto.getDesc());

            paths.add(path);
        }

        return paths;
    }
}
