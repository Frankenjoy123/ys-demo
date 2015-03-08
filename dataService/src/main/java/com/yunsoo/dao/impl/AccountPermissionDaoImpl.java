package com.yunsoo.dao.impl;

import com.yunsoo.dao.AccountPermissionDao;
import com.yunsoo.dao.PermissionDao;
import com.yunsoo.dbmodel.AccountPermissionModel;
import com.yunsoo.dbmodel.PermissionModel;
import com.yunsoo.util.ConvertHelper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author KB
 */
@Repository("permissionDAO")
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
