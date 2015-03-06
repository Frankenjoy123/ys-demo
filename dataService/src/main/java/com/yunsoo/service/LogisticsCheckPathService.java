package com.yunsoo.service;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.service.contract.LogisticsCheckPath;

import java.util.List;

/**
 * Created by Chen Jerry on 3/4/2015.
 */
public interface LogisticsCheckPathService {

    public LogisticsCheckPath get(long id);

    public Long save(LogisticsCheckPath logisticsCheckPath);

    public ServiceOperationStatus update(LogisticsCheckPath logisticsCheckPath);

    public boolean delete(long id, int deleteStatus);

    public List<LogisticsCheckPath> getLogisticsCheckPathsOrderByStartDate(String productKey);
}
