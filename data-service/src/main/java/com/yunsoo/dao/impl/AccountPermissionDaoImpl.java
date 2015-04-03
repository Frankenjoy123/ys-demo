package com.yunsoo.dao.impl;

import com.yunsoo.dao.AccountPermissionDao;
import com.yunsoo.dbmodel.AccountPermissionModel;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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
    public AccountPermissionModel get(long orgId, long accountId) {
        String hql = "select ap from AccountPermissionModel ap left join ap.accountOrg ao left join ao.account a left join ao.org aoo where a.id = :accountId and aoo.id = :orgId";
        Query query = sessionFactory.getCurrentSession().createQuery(hql).setLong("accountId", accountId).setLong("orgId", orgId);

        List<AccountPermissionModel> items = (List<AccountPermissionModel>) query.list();
        if (items != null && !items.isEmpty()) {
            return items.get(0);
        }
        return null;
    }
}
