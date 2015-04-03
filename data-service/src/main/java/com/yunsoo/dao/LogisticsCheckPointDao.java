package com.yunsoo.dao;

import com.yunsoo.dbmodel.LogisticsCheckPointModel;

import java.util.List;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
public interface LogisticsCheckPointDao {
    public LogisticsCheckPointModel get(int id);

    public LogisticsCheckPointModel get(String name);

    public int save(LogisticsCheckPointModel logisticsModel);

    public DaoStatus update(LogisticsCheckPointModel logisticsModel);

    public DaoStatus delete(int id, int deleteStatus);

    public List<LogisticsCheckPointModel> getAllLogisticsCheckPointModels();
}
