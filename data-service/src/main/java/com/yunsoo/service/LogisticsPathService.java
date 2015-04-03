package com.yunsoo.service;

import com.yunsoo.service.contract.LogisticsPath;

import java.util.List;

/**
 * Created by Jerry on 3/23/2015.
 */
public interface LogisticsPathService {
    public List<LogisticsPath> get(String productKey);

    public void save(LogisticsPath logisticsPath);

    public void save(List<LogisticsPath> logisticsPathList);

    public void update(LogisticsPath logisticsPath);
}
