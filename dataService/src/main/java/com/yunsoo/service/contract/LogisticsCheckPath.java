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
    private int status_id;
    private int startCheckPoint;
    private int endCheckPoint;
    private String startDate;
    private String desc;
    private String endDate;
    private long operator;
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

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getStartCheckPoint() {
        return startCheckPoint;
    }

    public void setStartCheckPoint(int startCheckPoint) {
        this.startCheckPoint = startCheckPoint;
    }

    public int getEndCheckPoint() {
        return endCheckPoint;
    }

    public void setEndCheckPoint(int endCheckPoint) {
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

    public long getOperator() {
        return operator;
    }

    public void setOperator(long operator) {
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

        if (model.getStartDate() != null) {
            path.setStartDate(model.getStartDate().toString());
        }
        path.setEndCheckPoint(model.getEndCheckPoint());

        if (model.getEndDate() != null) {
            path.setEndDate(model.getEndDate().toString());
        }
        path.setOperator(model.getOperator());

        return path;
    }

    public static LogisticsCheckPathModel ToModel(LogisticsCheckPath path) {
        if (path == null) return null;

        LogisticsCheckPathModel model = new LogisticsCheckPathModel();
        if (path.getId() >= 0) {
            model.setId(path.getId());
        }
        model.setId(path.getId());
        model.setProductKey(path.getProductKey());
        model.setStartCheckPoint(path.getStartCheckPoint());

        if (path.getStartDate() != null) {
            model.setStartDate(DateTime.parse(path.getStartDate()));
        }

        model.setEndCheckPoint(path.getEndCheckPoint());

        if (path.getEndDate() != null) {
            model.setEndDate(DateTime.parse(path.getEndDate()));
        }
        model.setOperator(path.getOperator());

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
