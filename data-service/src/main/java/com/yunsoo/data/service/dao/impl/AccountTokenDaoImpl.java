package com.yunsoo.data.service.dao.impl;

import com.yunsoo.data.service.dao.DaoStatus;
import com.yunsoo.data.service.dao.AccountTokenDao;
import com.yunsoo.data.service.dbmodel.AccountTokenModel;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public AccountTokenModel getByIdentifier(String identifier) {
        String hql = "select at from AccountTokenModel at left join at.account a where a.identifier = :identifier";
        Query query = sessionFactory.getCurrentSession().createQuery(hql).setString("identifier", identifier);
        List<AccountTokenModel> items = (List<AccountTokenModel>) query.list();
        if (items != null && !items.isEmpty()) {
            AccountTokenModel item = items.get(0);
            return item;
        }
        return null;
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
