package com.yunsoo.data.service.service.Impl;

import com.yunsoo.data.service.service.LogisticsPathService;
import com.yunsoo.data.service.dao.LogisticsPathDao;
import com.yunsoo.data.service.service.contract.LogisticsPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jerry on 3/23/2015.
 */
@Service("logisticsPathService")
public class LogisticsPathServiceImpl implements LogisticsPathService {

    @Autowired
    private LogisticsPathDao logisticsPathDao;

    @Override
    public List<LogisticsPath> get(String productKey) {
        return LogisticsPath.FromModelList(logisticsPathDao.get(productKey));
    }

    @Override
    public void save(LogisticsPath logisticsPath) {
        logisticsPathDao.save(LogisticsPath.ToModel(logisticsPath));
    }

    @Override
    public void save(List<LogisticsPath> paths) {
        logisticsPathDao.batchSave(LogisticsPath.ToModelList(paths));
    }

    @Override
    public void update(LogisticsPath logisticsPath) {
        logisticsPathDao.update(LogisticsPath.ToModel(logisticsPath));
    }
}
