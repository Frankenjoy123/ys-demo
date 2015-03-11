package com.yunsoo.dao.impl;

import com.yunsoo.dao.AccountPermissionDao;
import com.yunsoo.dbmodel.AccountPermissionModel;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author KB
 */
@Repository("accountPermissionDAO")
@Transactional
public class AccountPermissionDaoImpl implements AccountPermissionDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public AccountPermissionModel getByAccountOrg(long accountOrgId) {

        String hql = "from AccountPermissionModel where accountOrgId=" + accountOrgId;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        List<AccountPermissionModel> items = (List<AccountPermissionModel>) query.list();
        if (items != null && !items.isEmpty()) {
            return items.get(0);
        }
        return null;
    }
}
