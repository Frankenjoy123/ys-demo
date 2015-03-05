package com.yunsoo.service.Impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.LogisticsCheckPathDao;
import com.yunsoo.service.LogisticsCheckPathService;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.contract.LogisticsCheckPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Chen Jerry on 3/4/2015.
 */
@Service("logisticsCheckPathService")
@Transactional
public class LogisticsCheckPathServiceImpl implements LogisticsCheckPathService{

    @Autowired
    private LogisticsCheckPathDao logisticsCheckPathDao;

    @Override
    public LogisticsCheckPath get(Long id) {
        return LogisticsCheckPath.FromModel(logisticsCheckPathDao.get(id));
    }

    @Override
    public Long save(LogisticsCheckPath path) {
        if (path == null ) {
            return -1l;
        }

        return logisticsCheckPathDao.save(LogisticsCheckPath.ToModel(path));
    }

    @Override
    public ServiceOperationStatus update(LogisticsCheckPath path) {
        if (path == null || path.getId() < 0) {
            return ServiceOperationStatus.InvalidArgument;
        }
        DaoStatus daoStatus = logisticsCheckPathDao.update(LogisticsCheckPath.ToModel(path));
        if (daoStatus == DaoStatus.success) return ServiceOperationStatus.Success;
        else return ServiceOperationStatus.Fail;

    }

    @Override
    public boolean delete(Long id, int deleteStatus) {
        DaoStatus daoStatus = logisticsCheckPathDao.delete(id, deleteStatus);
        if (daoStatus == DaoStatus.success) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<LogisticsCheckPath> getLogisticsCheckPathsOrderByStartDate(String productKey) {
        return LogisticsCheckPath.FromModelList(logisticsCheckPathDao.getLogisticsCheckPathModelsOrderByStartDate(productKey));
    }
}
