package com.yunsoo.service;

import com.yunsoo.dbmodel.ProductPackageModel;
import com.yunsoo.service.contract.LogisticsCreated;
import com.yunsoo.service.contract.LogisticsInfo;
import com.yunsoo.service.contract.LogisticsPath;
import com.yunsoo.service.contract.ProductPackage;
import java.util.List;

/**
 * Created by: Lijian Created on: 2015/2/1 Descriptions:
 */
public interface LogisticsService {

    public LogisticsInfo query(String key);

    public boolean startTracking(LogisticsCreated entity);

    public boolean logPath(LogisticsPath path);

}
