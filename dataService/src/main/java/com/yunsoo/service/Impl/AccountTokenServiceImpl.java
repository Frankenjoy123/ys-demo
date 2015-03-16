package com.yunsoo.service.Impl;

import com.yunsoo.dao.AccountTokenDao;
import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dbmodel.AccountTokenModel;
import com.yunsoo.service.AccountTokenService;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.contract.AccountToken;
import com.yunsoo.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Jerry on 3/15/2015.
 */
@Service("accountTokenService")
public class AccountTokenServiceImpl implements AccountTokenService {

    @Autowired
    private AccountTokenDao accountTokenDao;

    @Override
    public AccountToken get(long id) {
        return AccountToken.FromModel(accountTokenDao.get(id));
    }

    @Override
    public long save(AccountToken accountToken) {
        if (accountToken == null ) {
            return -1;
        }
        return accountTokenDao.save(AccountToken.ToModel(accountToken));
    }

    @Override
    public ServiceOperationStatus update(AccountToken accountToken) {
        if (accountToken == null || accountToken.getId() < 0) {
            return ServiceOperationStatus.InvalidArgument;
        }

        AccountTokenModel model = new AccountTokenModel();
        BeanUtils.copyProperties(accountToken, model, SpringBeanUtil.getNullPropertyNames(accountToken));

        DaoStatus daoStatus = accountTokenDao.update(model);
        if (daoStatus == DaoStatus.success) return ServiceOperationStatus.Success;
        else return ServiceOperationStatus.Fail;
    }

    @Override
    public boolean delete(int id, int deleteStatus) {
        DaoStatus daoStatus = accountTokenDao.delete(id, deleteStatus);
        if (daoStatus == DaoStatus.success) {
            return true;
        } else {
            return false;
        }
    }
}
