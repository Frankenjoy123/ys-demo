package com.yunsoo.data.service.service.Impl;

import com.yunsoo.data.service.dao.DaoStatus;
import com.yunsoo.data.service.dao.LogisticsCheckPathDao;
import com.yunsoo.data.service.dbmodel.LogisticsCheckPathModel;
import com.yunsoo.data.service.service.LogisticsCheckPathService;
import com.yunsoo.data.service.service.ServiceOperationStatus;
import com.yunsoo.data.service.service.contract.LogisticsCheckPath;
import com.yunsoo.data.service.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;
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
    public LogisticsCheckPath get(long id) {
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
    public Long save(List<LogisticsCheckPath> paths) {
        if (paths == null || paths.isEmpty()) {
            return -1l;
        }

        for (LogisticsCheckPath path : paths)
            logisticsCheckPathDao.save(LogisticsCheckPath.ToModel(path));

        return 0l;
    }

    @Override
    public ServiceOperationStatus update(LogisticsCheckPath path) {
        if (path == null || path.getId() < 0) {
            return ServiceOperationStatus.InvalidArgument;
        }

        LogisticsCheckPathModel model = new LogisticsCheckPathModel();
        BeanUtils.copyProperties(path, model,SpringBeanUtil.getNullPropertyNames(path));

        DaoStatus daoStatus = logisticsCheckPathDao.update(model);
        if (daoStatus == DaoStatus.success) return ServiceOperationStatus.Success;
        else return ServiceOperationStatus.Fail;

    }

    @Override
    public boolean delete(long id, int deleteStatus) {
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
