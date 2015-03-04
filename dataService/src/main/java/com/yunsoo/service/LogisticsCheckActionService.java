package com.yunsoo.service;

import com.yunsoo.service.contract.LogisticsCheckAction;

import java.util.List;

/**
 * Created by Chen Jerry on 3/4/2015.
 */
public interface LogisticsCheckActionService {
    public LogisticsCheckAction get(int id);

    public LogisticsCheckAction get(String name);

    public int save(LogisticsCheckAction action);

    public ServiceOperationStatus update(LogisticsCheckAction action);

    public boolean delete(int id, int deleteStatus);

    public List<LogisticsCheckAction> getAllLogisticsCheckActions();

    public List<LogisticsCheckAction> getLogisticsCheckActionsByFilter(int id, String name);
}
