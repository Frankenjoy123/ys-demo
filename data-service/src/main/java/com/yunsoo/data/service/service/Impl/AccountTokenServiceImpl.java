package com.yunsoo.data.service.service.Impl;

import com.yunsoo.data.service.dao.AccountDao;
import com.yunsoo.data.service.dao.AccountTokenDao;
import com.yunsoo.data.service.dao.DaoStatus;
import com.yunsoo.data.service.service.AccountTokenService;
import com.yunsoo.data.service.service.ServiceOperationStatus;
import com.yunsoo.data.service.service.contract.Account;
import com.yunsoo.data.service.service.contract.AccountToken;
import com.yunsoo.data.service.dbmodel.AccountTokenModel;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Jerry on 3/15/2015.
 */
@Service("accountTokenService")
public class AccountTokenServiceImpl implements AccountTokenService {

    @Autowired
    private AccountTokenDao accountTokenDao;
    @Autowired
    private AccountDao accountDao;

    @Override
    public AccountToken get(long id) {
        return AccountToken.FromModel(accountTokenDao.get(id));
    }

    @Override
    public AccountToken getByIdentifier(String identifier) {
        return AccountToken.FromModel(accountTokenDao.getByIdentifier(identifier));
    }

    @Override
    public long save(AccountToken accountToken) {
        if (accountToken == null ) {
            return -1;
        }
        if (accountToken.getAccount() == null) {
            return -1;
        }
        Account account = accountToken.getAccount();
        String identifier = account.getIdentifier();
        if (identifier != null) {
            accountToken.setAccount(Account.FromModel(accountDao.getByIdentifier(identifier)));
        }

        DateTime now = DateTime.now();
        accountToken.setAccessToken(UUID.randomUUID().toString());
        accountToken.setAccessTokenTs(now);
        accountToken.setAccessTokenExpires(now.plusDays(30));
        accountToken.setRefreshToken(UUID.randomUUID().toString());
        accountToken.setRefreshTokenTs(now);
        accountToken.setRefreshTokenExpires(now.plusDays(60));

        return accountTokenDao.save(AccountToken.ToModel(accountToken));
    }

    @Override
    public ServiceOperationStatus update(AccountToken accountToken) {
        if (accountToken == null || accountToken.getId() < 0) {
            return ServiceOperationStatus.InvalidArgument;
        }

        AccountTokenModel model = accountTokenDao.get(accountToken.getId());
        DateTime now = DateTime.now();
        model.setAccessToken(UUID.randomUUID().toString());
        model.setAccessTokenTs(now);
        model.setAccessTokenExpires(now.plusDays(30));
        model.setRefreshToken(UUID.randomUUID().toString());
        model.setRefreshTokenTs(now);
        model.setRefreshTokenExpires(now.plusDays(60));

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