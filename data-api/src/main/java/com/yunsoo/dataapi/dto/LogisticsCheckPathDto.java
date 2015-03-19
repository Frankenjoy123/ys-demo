package com.yunsoo.dataapi.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
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
    private Integer action_id;
    private Integer startCheckPoint;
    private Integer endCheckPoint;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime startDate;
    private String desc;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime endDate;
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

    public Integer getAction_id() {
        return action_id;
    }

    public void setAction_id(Integer action_id) {
        this.action_id = action_id;
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

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
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
        pathDto.setStartDate(path.getStartDate());
        pathDto.setEndCheckPoint(path.getEndCheckPoint());
        pathDto.setEndDate(path.getEndDate());
        pathDto.setOperator(path.getOperator());
        pathDto.setAction_id(path.getAction_id());
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
        path.setEndDate(pathDto.getEndDate());
        path.setOperator(pathDto.getOperator());
        path.setAction_id(pathDto.getAction_id());
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

    public static List<LogisticsCheckPath> TOLogisticsCheckPathList(List<LogisticsCheckPathDto> pathDtoList) {
        if (pathDtoList == null) return null;

        List<LogisticsCheckPath> pathList = new ArrayList<LogisticsCheckPath>();
        for (LogisticsCheckPathDto pathDto : pathDtoList) {
            pathList.add(LogisticsCheckPathDto.ToLogisticsCheckPath(pathDto));
        }

        return pathList;
    }
}