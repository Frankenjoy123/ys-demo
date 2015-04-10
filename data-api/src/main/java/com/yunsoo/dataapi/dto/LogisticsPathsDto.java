package com.yunsoo.dataapi.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.data.service.service.contract.LogisticsCheckPath;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Jerry on 3/6/2015.
 */
public class LogisticsPathsDto {
    private long Id;
    private List<String> productKeys;
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

    public List<String> getProductKey() {
        return productKeys;
    }

    public void setProductKey(List<String> productKeys) {
        this.productKeys = productKeys;
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

    public static List<LogisticsCheckPath> ToLogisticsCheckPath(LogisticsPathsDto pathsDto) {
        if (pathsDto == null) return null;

        List<String> productKeysTemp = pathsDto.getProductKey();
        if(productKeysTemp == null || productKeysTemp.isEmpty()) return null;

        List<LogisticsCheckPath> paths = new ArrayList<LogisticsCheckPath>();
        for (String key : productKeysTemp) {
            LogisticsCheckPath path = new LogisticsCheckPath();
            path.setProductKey(key);

            //Use deviceId to get start check point
            path.setStartCheckPoint(pathsDto.getStartCheckPoint());

            //Record the start date as the server date
            path.setStartDate(DateTime.now());

            path.setEndCheckPoint(pathsDto.getEndCheckPoint());

            path.setEndDate(pathsDto.getEndDate());

            path.setOperator(pathsDto.getOperator());
            path.setAction_id(pathsDto.getAction_id());
            path.setDesc(pathsDto.getDesc());

            paths.add(path);
        }

        return paths;
    }
}
