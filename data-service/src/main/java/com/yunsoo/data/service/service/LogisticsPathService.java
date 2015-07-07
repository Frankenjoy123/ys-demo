package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.LogisticsPath;

import java.util.List;

/**
 * Created by  : Jerry
 * Created on  : 3/23/2015
 * Descriptions:
 */
public interface LogisticsPathService {
   List<LogisticsPath> getByProductKey(String productKey);

   void save(LogisticsPath logisticsPath);

   void save(List<LogisticsPath> logisticsPathList);

   void update(LogisticsPath logisticsPath);
}
