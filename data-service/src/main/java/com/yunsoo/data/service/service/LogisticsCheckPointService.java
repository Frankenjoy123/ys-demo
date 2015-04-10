package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.LogisticsCheckPoint;

import java.util.List;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
public interface LogisticsCheckPointService {
    public LogisticsCheckPoint get(int id);

    public LogisticsCheckPoint get(String name);

    public int save(LogisticsCheckPoint action);

    public ServiceOperationStatus update(LogisticsCheckPoint action);

    public boolean delete(int id, int deleteStatus);

    public List<LogisticsCheckPoint> getAllLogisticsCheckPoints();
}
