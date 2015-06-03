package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.dynamodb.LogisticsPathModel;

import java.util.List;

/**
 * Created by  : Jerry
 * Created on  : 3/23/2015
 * Descriptions:
 */
public interface LogisticsPathDao {

    List<LogisticsPathModel> get(String productKey);

    void save(LogisticsPathModel logisticsModel);

    void batchSave(List<LogisticsPathModel> logisticsPathModelList);

    void update(LogisticsPathModel logisticsModel);
}
