package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.LogisticsCheckActionModel;

import java.util.List;

/**
 * Created by Chen Jerry on 3/3/2015.
 */
public interface LogisticsCheckActionDao {
    public LogisticsCheckActionModel get(int id);

    public LogisticsCheckActionModel get(String name);

    public int save(LogisticsCheckActionModel logisticsModel);

    public DaoStatus update(LogisticsCheckActionModel logisticsModel);

    public DaoStatus delete(int id, int deleteStatus);

    public List<LogisticsCheckActionModel> getAllLogisticsCheckActionModels();

    public List<LogisticsCheckActionModel> getLogisticsCheckActionModelsByFilter(int id, String name);

    public List<LogisticsCheckActionModel> getLogisticsCheckActionModelsByOrg(Long orgId);
}
