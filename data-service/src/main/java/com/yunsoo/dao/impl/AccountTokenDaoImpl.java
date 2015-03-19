package com.yunsoo.dao.impl;

import com.yunsoo.dao.AccountTokenDao;
import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dbmodel.AccountTokenModel;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Jerry on 3/15/2015.
 */
@Repository("accountTokenDao")
@Transactional
public class AccountTokenDaoImpl implements AccountTokenDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public AccountTokenModel get(long id) {
        return (AccountTokenModel) sessionFactory.getCurrentSession().get(
                AccountTokenModel.class, id);
    }

    @Override
    public long save(AccountTokenModel accountTokenModel) {
        sessionFactory.getCurrentSession().save(accountTokenModel);
        return accountTokenModel.getId();
    }

    @Override
    public DaoStatus update(AccountTokenModel accountTokenModel) {
        try {
            sessionFactory.getCurrentSession().update(accountTokenModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log ex
            return DaoStatus.fail;
        }
    }

    @Override
    public DaoStatus delete(long Id, int deleteStatus) {
        try {
            AccountTokenModel accountTokenModel = this.get(Id);
            if (accountTokenModel == null) return DaoStatus.NotFound;

            sessionFactory.getCurrentSession().delete(accountTokenModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log ex
            return DaoStatus.fail;
        }
    }
}
