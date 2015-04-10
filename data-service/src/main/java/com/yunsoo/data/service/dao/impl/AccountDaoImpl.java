package com.yunsoo.data.service.dao.impl;

import com.yunsoo.data.service.dao.AccountDao;
import com.yunsoo.data.service.dbmodel.AccountModel;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author KB
 */
@Repository("accountDAO")
@Transactional
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public AccountModel getByToken(String token) {
        String hql = "select a from AccountModel a left join a.tokens at where at.accessToken = :token";
        Query query = sessionFactory.getCurrentSession().createQuery(hql).setString("token", token);
        List<AccountModel> items = (List<AccountModel>) query.list();
        if (items != null && !items.isEmpty()) {
            return items.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    public AccountModel getByIdentifier(String identifier) {
        String hql = "from AccountModel where identifier = :identifier";
        Query query = sessionFactory.getCurrentSession().createQuery(hql).setString("identifier", identifier);

        List<AccountModel> items = (List<AccountModel>) query.list();
        if (items != null && !items.isEmpty()) {
            return items.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    public AccountModel get(long id) {
        String hql = "select a from AccountModel a where id = " + id;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        List<AccountModel> items = (List<AccountModel>) query.list();
        if (items != null && !items.isEmpty()) {
            return items.get(0);
        }
        return null;
    }
}
