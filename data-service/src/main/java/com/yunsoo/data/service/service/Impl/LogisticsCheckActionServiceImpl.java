package com.yunsoo.data.service.service.Impl;

import com.yunsoo.data.service.dao.DaoStatus;
import com.yunsoo.data.service.dao.LogisticsCheckActionDao;
import com.yunsoo.data.service.dbmodel.LogisticsCheckActionModel;
import com.yunsoo.data.service.service.LogisticsCheckActionService;
import com.yunsoo.data.service.service.ServiceOperationStatus;
import com.yunsoo.data.service.service.contract.LogisticsCheckAction;
import com.yunsoo.data.service.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Chen Jerry on 3/4/2015.
 */
@Service("logisticsCheckActionService")
public class LogisticsCheckActionServiceImpl implements LogisticsCheckActionService {

    @Autowired
    private LogisticsCheckActionDao logisticsCheckActionDao;

    @Override
    public LogisticsCheckAction get(int id) {
        return LogisticsCheckAction.FromModel(logisticsCheckActionDao.get(id));
    }

    @Override
    public LogisticsCheckAction get(String name) {
        return LogisticsCheckAction.FromModel(logisticsCheckActionDao.get(name));
    }

    @Override
    public int save(LogisticsCheckAction action) {
        if (action == null ) {
            return -1;
        }
        return logisticsCheckActionDao.save(LogisticsCheckAction.ToModel(action));
    }

    @Override
    public ServiceOperationStatus update(LogisticsCheckAction action) {
        if (action == null || action.getId() < 0) {
            return ServiceOperationStatus.InvalidArgument;
        }
        LogisticsCheckActionModel model = new LogisticsCheckActionModel();
        BeanUtils.copyProperties(action, model,SpringBeanUtil.getNullPropertyNames(action));

        DaoStatus daoStatus = logisticsCheckActionDao.update(model);
        if (daoStatus == DaoStatus.success) return ServiceOperationStatus.Success;
        else return ServiceOperationStatus.Fail;

    }

    @Override
    public boolean delete(int id, int deleteStatus) {
        DaoStatus daoStatus = logisticsCheckActionDao.delete(id, deleteStatus);
        if (daoStatus == DaoStatus.success) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public List<LogisticsCheckAction> getAllLogisticsCheckActions() {
        return LogisticsCheckAction.FromModelList(logisticsCheckActionDao.getAllLogisticsCheckActionModels());
    }

    @Override
    @Transactional
    public List<LogisticsCheckAction> getLogisticsCheckActionsByFilter(int id, String name) {
        return LogisticsCheckAction.FromModelList(logisticsCheckActionDao.getLogisticsCheckActionModelsByFilter(id, name));
    }

    @Override
    public List<LogisticsCheckAction> getLogisticsCheckActionsByOrg(Long orgId)
    {
        return LogisticsCheckAction.FromModelList(logisticsCheckActionDao.getLogisticsCheckActionModelsByOrg(orgId));
    }
}
