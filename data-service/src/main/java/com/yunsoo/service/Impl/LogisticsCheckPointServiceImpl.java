package com.yunsoo.service.Impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.LogisticsCheckPointDao;
import com.yunsoo.dbmodel.LogisticsCheckPointModel;
import com.yunsoo.service.LogisticsCheckPointService;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.contract.LogisticsCheckPoint;
import com.yunsoo.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
@Service("logisticsCheckPointService")
public class LogisticsCheckPointServiceImpl implements LogisticsCheckPointService {
    @Autowired
    private LogisticsCheckPointDao logisticsCheckPointDao;

    @Override
    public LogisticsCheckPoint get(int id) {
        return LogisticsCheckPoint.FromModel(logisticsCheckPointDao.get(id));
    }

    @Override
    public LogisticsCheckPoint get(String name) {
        return LogisticsCheckPoint.FromModel(logisticsCheckPointDao.get(name));
    }

    @Override
    public int save(LogisticsCheckPoint point) {
        if (point == null ) {
            return -1;
        }
        return logisticsCheckPointDao.save(LogisticsCheckPoint.ToModel(point));
    }

    @Override
    public ServiceOperationStatus update(LogisticsCheckPoint point) {
        if (point == null || point.getId() < 0) {
            return ServiceOperationStatus.InvalidArgument;
        }

        LogisticsCheckPointModel model = new LogisticsCheckPointModel();
        BeanUtils.copyProperties(point, model,SpringBeanUtil.getNullPropertyNames(point));

        DaoStatus daoStatus = logisticsCheckPointDao.update(model);
        if (daoStatus == DaoStatus.success) return ServiceOperationStatus.Success;
        else return ServiceOperationStatus.Fail;
    }

    @Override
    public boolean delete(int id, int deleteStatus) {
        DaoStatus daoStatus = logisticsCheckPointDao.delete(id, deleteStatus);
        if (daoStatus == DaoStatus.success) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public List<LogisticsCheckPoint> getAllLogisticsCheckPoints() {
        return LogisticsCheckPoint.FromModelList(logisticsCheckPointDao.getAllLogisticsCheckPointModels());
    }

}
