package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.dbmodel.dynamodb.LogisticsPathModel;
import org.joda.time.DateTime;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Chen Jerry
 * Created on  : 3/23/2015
 * Descriptions:
 */
public class LogisticsPath {

    private String productKey;
    private String actionId;
    private String startCheckPoint;
    private DateTime startDateTime;
    private String endCheckPoint;
    private DateTime endDateTime;
    private String operator;
    private String deviceId;
    private String description;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getStartCheckPoint() {
        return startCheckPoint;
    }

    public void setStartCheckPoint(String startCheckPoint) {
        this.startCheckPoint = startCheckPoint;
    }

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(DateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndCheckPoint() {
        return endCheckPoint;
    }

    public void setEndCheckPoint(String endCheckPoint) {
        this.endCheckPoint = endCheckPoint;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static LogisticsPath FromModel(LogisticsPathModel model) {
        if (model == null) return null;

        LogisticsPath path = new LogisticsPath();
        path.setProductKey(model.getProductKey());
        path.setActionId(model.getActionId());
        path.setStartCheckPoint(model.getStartCheckPoint());
        path.setStartDateTime(model.getStartDateTime());
        path.setEndCheckPoint(model.getEndCheckPoint());
        path.setEndDateTime(model.getEndDateTime());
        path.setOperator(model.getOperator());
        path.setDeviceId(model.getDeviceId());
        path.setDescription(model.getDescription());

        return path;
    }

    public static LogisticsPathModel ToModel(LogisticsPath path) {
        if (path == null) return null;

        LogisticsPathModel model = new LogisticsPathModel();
        model.setProductKey(path.getProductKey());
        model.setActionId(path.getActionId());
        model.setStartCheckPoint(path.getStartCheckPoint());
        model.setStartDateTime(path.getStartDateTime());
        model.setEndCheckPoint(path.getEndCheckPoint());
        model.setEndDateTime(path.getEndDateTime());
        model.setOperator(path.getOperator());
        model.setDeviceId(path.getDeviceId());
        model.setDescription(path.getDescription());

        return model;
    }

    public static List<LogisticsPath> FromModelList(List<LogisticsPathModel> modelList) {
        if (modelList == null) return null;

        return modelList.stream().map(LogisticsPath::FromModel).collect(Collectors.toList());
    }

    public static List<LogisticsPathModel> ToModelList(List<LogisticsPath> pathList) {
        if (pathList == null) return null;

        return pathList.stream().map(LogisticsPath::ToModel).collect(Collectors.toList());
    }
}
