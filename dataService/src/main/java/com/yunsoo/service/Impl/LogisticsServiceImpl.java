/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yunsoo.service.Impl;

import com.yunsoo.dao.LogisticsDao;
import com.yunsoo.dbmodel.LogisticsTrackingModel;
import com.yunsoo.service.LogisticsService;
import com.yunsoo.service.contract.LogisticsCreated;
import com.yunsoo.service.contract.LogisticsInfo;
import com.yunsoo.service.contract.LogisticsPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author qyu
 */
@Service("LogisticsService")
public class LogisticsServiceImpl implements LogisticsService{

    @Autowired
    private LogisticsDao logisticsDao;
    
    @Override
    public LogisticsInfo query(String key) {
        LogisticsTrackingModel model =  logisticsDao.getTrackingInfo(key);
       
        LogisticsInfo info = new LogisticsInfo();
        return info;
    }

    @Override
    public boolean startTracking(LogisticsCreated entity) {
        return true;
    }   

    @Override
    public boolean logPath(LogisticsPath path) {
        return true;
    }
    
}
