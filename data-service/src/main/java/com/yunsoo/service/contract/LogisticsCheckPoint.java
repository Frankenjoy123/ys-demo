package com.yunsoo.service.contract;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.yunsoo.dbmodel.LogisticsCheckPointModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
public class LogisticsCheckPoint {

    private int id;
    private String name;
    private String description;
    private int locationId;
    private Long orgId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public static LogisticsCheckPoint FromModel(LogisticsCheckPointModel model) {
        if (model == null) return null;
        LogisticsCheckPoint point = new LogisticsCheckPoint();
        point.setId(model.getId());
        point.setName(model.getName());
        point.setDescription(model.getDescription());
        point.setLocationId(model.getLocation_id());
        point.setOrgId(model.getOrg_id());

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
        model.setLocation_id(point.getLocationId());
        model.setOrg_id(point.getOrgId());

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
