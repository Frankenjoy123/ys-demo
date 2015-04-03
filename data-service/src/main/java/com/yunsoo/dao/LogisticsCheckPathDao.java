package com.yunsoo.dao;

import com.yunsoo.dbmodel.LogisticsCheckPathModel;

import java.util.List;

/**
 * Created by Chen Jerry on 3/4/2015.
 */
public interface LogisticsCheckPathDao {

    public LogisticsCheckPathModel get(long id);

    public Long save(LogisticsCheckPathModel logisticsModel);

    public DaoStatus update(LogisticsCheckPathModel logisticsModel);

    public DaoStatus delete(long id, int deleteStatus);

    public List<LogisticsCheckPathModel> getLogisticsCheckPathModelsOrderByStartDate(String productKey);
}
