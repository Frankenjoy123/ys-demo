package com.yunsoo.dao;

import com.yunsoo.dbmodel.LogisticsPathModel;

import java.util.List;

/**
 * Created by Jerry on 3/23/2015.
 */
public interface LogisticsPathDao {
    public List<LogisticsPathModel> get(String productKey);

    public void save(LogisticsPathModel logisticsModel);

    public void batchSave(List<LogisticsPathModel> logisticsPathModelList);

    public void update(LogisticsPathModel logisticsModel);
}
