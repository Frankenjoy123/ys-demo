package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.LogisticsCheckPathModel;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Jerry on 3/4/2015.
 */
public class LogisticsCheckPath {

    private long Id;
    private String productKey;
    private Integer status_id;
    private Integer startCheckPoint;
    private Integer endCheckPoint;
    private DateTime startDate;
    private String desc;
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

    public static LogisticsCheckPath FromModel(LogisticsCheckPathModel model) {
        if (model == null) return null;

        LogisticsCheckPath path = new LogisticsCheckPath();
        path.setId(model.getId());
        path.setProductKey(model.getProductKey());
        path.setStartCheckPoint(model.getStartCheckPoint());
        path.setStartDate(model.getStartDate());
        path.setEndCheckPoint(model.getEndCheckPoint());
        path.setEndDate(model.getEndDate());
        path.setOperator(model.getOperator());
        path.setStatus_id(model.getStatus_id());
        path.setDesc(model.getDesc());

        return path;
    }

    public static LogisticsCheckPathModel ToModel(LogisticsCheckPath path) {
        if (path == null) return null;

        LogisticsCheckPathModel model = new LogisticsCheckPathModel();
        model.setId(path.getId());
        model.setProductKey(path.getProductKey());
        model.setStartCheckPoint(path.getStartCheckPoint());
        model.setStartDate(path.getStartDate());
        model.setEndCheckPoint(path.getEndCheckPoint());
        model.setEndDate(path.getEndDate());
        model.setOperator(path.getOperator());
        model.setStatus_id(path.getStatus_id());
        model.setDesc(path.getDesc());

        return model;
    }

    public static List<LogisticsCheckPath> FromModelList(List<LogisticsCheckPathModel> modelList) {
        if (modelList == null) return null;

        List<LogisticsCheckPath> pathList = new ArrayList<LogisticsCheckPath>();
        for (LogisticsCheckPathModel model : modelList) {
            pathList.add(LogisticsCheckPath.FromModel(model));
        }

        return pathList;
    }

    public static List<LogisticsCheckPathModel> ToModelList(List<LogisticsCheckPath> pathList) {
        if (pathList == null) return null;

        List<LogisticsCheckPathModel> modelList = new ArrayList<LogisticsCheckPathModel>();
        for (LogisticsCheckPath path : pathList) {
            modelList.add(LogisticsCheckPath.ToModel(path));
        }

        return modelList;
    }
}
