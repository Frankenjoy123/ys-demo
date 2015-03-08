package com.yunsoo.dataapi.dto;

import com.yunsoo.service.contract.LogisticsCheckPath;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Jerry on 3/5/2015.
 */
public class LogisticsCheckPathDto {
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
        this.Id = id;
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

    public static LogisticsCheckPathDto FromLogisticsCheckPath(LogisticsCheckPath path) {
        LogisticsCheckPathDto pathDto = new LogisticsCheckPathDto();

        pathDto.setId(path.getId());
        pathDto.setProductKey(path.getProductKey());
        pathDto.setStartCheckPoint(path.getStartCheckPoint());
        if(path.getStartDate() != null) {
            pathDto.setStartDate(path.getStartDate().toString("yyyy-MM-dd hh:mm:ss"));
        }
        pathDto.setEndCheckPoint(path.getEndCheckPoint());
        if(path.getEndDate() != null) {
            pathDto.setEndDate(path.getEndDate().toString("yyyy-MM-dd hh:mm:ss"));
        }
        pathDto.setOperator(path.getOperator());
        pathDto.setStatus_id(path.getStatus_id());
        pathDto.setDesc(path.getDesc());

        return pathDto;
    }

    public static LogisticsCheckPath ToLogisticsCheckPath(LogisticsCheckPathDto pathDto) {
        if (pathDto == null) return null;

        LogisticsCheckPath path = new LogisticsCheckPath();
        path.setId(pathDto.getId());

        //Need to get key list from key package
        path.setProductKey(pathDto.getProductKey());

        //Use deviceId to get start check point
        path.setStartCheckPoint(pathDto.getStartCheckPoint());

        //Record the start date as the server date
        path.setStartDate(DateTime.now());

        path.setEndCheckPoint(pathDto.getEndCheckPoint());

        if (pathDto.getEndDate()!=null && !pathDto.getEndDate().isEmpty()) {
            path.setEndDate(DateTime.parse(pathDto.getEndDate()));
        }
        path.setOperator(pathDto.getOperator());
        path.setStatus_id(pathDto.getStatus_id());
        path.setDesc(pathDto.getDesc());

        return path;
    }

    public static List<LogisticsCheckPathDto> FromLogisticsCheckPathList(List<LogisticsCheckPath> pathList) {
        if (pathList == null) return null;

        List<LogisticsCheckPathDto> pathDtoList = new ArrayList<LogisticsCheckPathDto>();
        for (LogisticsCheckPath path : pathList) {
            pathDtoList.add(LogisticsCheckPathDto.FromLogisticsCheckPath(path));
        }

        return pathDtoList;
    }
}