package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.dbmodel.dynamodb.LogisticsPathModel;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Jerry on 3/23/2015.
 */
public class LogisticsPath {

    private String productKey;
    private Integer action_id;
    private String startCheckPoint;
    private String endCheckPoint;
    private DateTime startDate;
    private String desc;
    private DateTime endDate;
    private String operator;
    private String deviceId;

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

    public String getStartCheckPoint() {
        return startCheckPoint;
    }

    public void setStartCheckPoint(String startCheckPoint) {
        this.startCheckPoint = startCheckPoint;
    }

    public String getEndCheckPoint() {
        return endCheckPoint;
    }

    public void setEndCheckPoint(String endCheckPoint) {
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
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

    public static LogisticsPath FromModel(LogisticsPathModel model) {
        if (model == null) return null;

        LogisticsPath path = new LogisticsPath();
        path.setProductKey(model.getProductKey());
        path.setStartCheckPoint(model.getStartCheckPoint());
        path.setStartDate(model.getStartDate());
        path.setEndCheckPoint(model.getEndCheckPoint());
        path.setEndDate(model.getEndDate());
        path.setOperator(model.getOperator());
        path.setAction_id(model.getAction_id());
        path.setDesc(model.getDesc());

        return path;
    }

    public static LogisticsPathModel ToModel(LogisticsPath path) {
        if (path == null) return null;

        LogisticsPathModel model = new LogisticsPathModel();
        model.setProductKey(path.getProductKey());
        model.setStartCheckPoint(path.getStartCheckPoint());
        model.setStartDate(path.getStartDate());
        model.setEndCheckPoint(path.getEndCheckPoint());
        model.setEndDate(path.getEndDate());
        model.setOperator(path.getOperator());
        model.setAction_id(path.getAction_id());
        model.setDesc(path.getDesc());

        return model;
    }

    public static List<LogisticsPath> FromModelList(List<LogisticsPathModel> modelList) {
        if (modelList == null) return null;

        List<LogisticsPath> pathList = new ArrayList<LogisticsPath>();
        for (LogisticsPathModel model : modelList) {
            pathList.add(LogisticsPath.FromModel(model));
        }

        return pathList;
    }

    public static List<LogisticsPathModel> ToModelList(List<LogisticsPath> pathList) {
        if (pathList == null) return null;

        List<LogisticsPathModel> modelList = new ArrayList<LogisticsPathModel>();
        for (LogisticsPath path : pathList) {
            modelList.add(LogisticsPath.ToModel(path));
        }

        return modelList;
    }
}
