package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.LogisticsCreated;
import com.yunsoo.data.service.service.contract.LogisticsInfo;
import com.yunsoo.data.service.service.contract.LogisticsPath;

/**
 * Created by: Lijian Created on: 2015/2/1 Descriptions:
 */
public interface LogisticsService {

    public LogisticsInfo query(String key);

    public boolean startTracking(LogisticsCreated entity);

    public boolean logPath(LogisticsPath path);

}
