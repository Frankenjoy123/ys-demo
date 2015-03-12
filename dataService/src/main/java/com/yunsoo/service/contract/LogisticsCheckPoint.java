package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.LogisticsCheckPointModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
public class LogisticsCheckPoint {

    private int Id;
    private String name;
    private String description;
    private int locationId;
    private int orgId;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLocation_id() {
        return locationId;
    }

    public void setLocation_id(int locationId) {
        this.locationId = locationId;
    }

    public int getOrg_id() {
        return orgId;
    }

    public void setOrg_id(int org_id) {
        this.orgId = org_id;
    }

    public static LogisticsCheckPoint FromModel(LogisticsCheckPointModel model) {
        if (model == null) return null;
        LogisticsCheckPoint point = new LogisticsCheckPoint();
        point.setId(model.getId());
        point.setName(model.getName());
        point.setDescription(model.getDescription());
        point.setLocation_id(model.getLocation_id());
        point.setOrg_id(model.getOrg_id());

        return point;
    }

    public static LogisticsCheckPointModel ToModel(LogisticsCheckPoint point) {
        if (point == null) return null;
        LogisticsCheckPointModel model = new LogisticsCheckPointModel();
        if (point.getId() >= 0) {
            model.setId(point.getId());
        }
        model.setId(point.getId());
        model.setName(point.getName());
        model.setDescription(point.getDescription());
        model.setLocation_id(point.getLocation_id());
        model.setOrg_id(point.getOrg_id());

        return model;
    }

    public static List<LogisticsCheckPoint> FromModelList(List<LogisticsCheckPointModel> modelList) {
        if (modelList == null) return null;
        List<LogisticsCheckPoint> pointList = new ArrayList<LogisticsCheckPoint>();
        for (LogisticsCheckPointModel model : modelList) {
            pointList.add(LogisticsCheckPoint.FromModel(model));
        }

        return pointList;
    }

    public static List<LogisticsCheckPointModel> ToModelList(List<LogisticsCheckPoint> pointList) {
        if (pointList == null) return null;
        List<LogisticsCheckPointModel> modelList = new ArrayList<LogisticsCheckPointModel>();
        for (LogisticsCheckPoint point : pointList) {
            modelList.add(LogisticsCheckPoint.ToModel(point));
        }

        return modelList;
    }
}
