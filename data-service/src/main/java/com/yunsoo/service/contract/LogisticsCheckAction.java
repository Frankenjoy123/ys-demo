package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.LogisticsCheckActionModel;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen Jerry on 3/4/2015.
 */
public class LogisticsCheckAction {
    private int id;
    private String name;
    private String description;
    private Long orgId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        id = id;
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

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public static LogisticsCheckAction FromModel(LogisticsCheckActionModel model) {
        if (model == null) return null;
        LogisticsCheckAction action = new LogisticsCheckAction();
        action.setId(model.getId());
        action.setName(model.getName());
        action.setDescription(model.getDescription());
        action.setOrgId(model.getOrgId());

        return action;
    }

    public static LogisticsCheckActionModel ToModel(LogisticsCheckAction action) {
        if (action == null) return null;
        LogisticsCheckActionModel model = new LogisticsCheckActionModel();
        if (action.getId() >= 0) {
            model.setId(action.getId());
        }
        model.setId(action.getId());
        model.setName(action.getName());
        model.setDescription(action.getDescription());
        model.setOrgId(action.getOrgId());

        return model;
    }

    public static List<LogisticsCheckAction> FromModelList(List<LogisticsCheckActionModel> modelList) {
        if (modelList == null) return null;
        List<LogisticsCheckAction> actionList = new ArrayList<LogisticsCheckAction>();
        for (LogisticsCheckActionModel model : modelList) {
            actionList.add(LogisticsCheckAction.FromModel(model));
        }

        return actionList;
    }

    public static List<LogisticsCheckActionModel> ToModelList(List<LogisticsCheckAction> actionList) {
        if (actionList == null) return null;
        List<LogisticsCheckActionModel> modelList = new ArrayList<LogisticsCheckActionModel>();
        for (LogisticsCheckAction action : actionList) {
            modelList.add(LogisticsCheckAction.ToModel(action));
        }

        return modelList;
    }
}
