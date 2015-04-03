package com.yunsoo.dao.impl;

import com.yunsoo.dao.PermissionDao;
import com.yunsoo.dbmodel.PermissionModel;
import com.yunsoo.util.ConvertHelper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author KB
 */
@Repository("permissionDAO")
@Transactional
public class PermissionDaoImpl implements PermissionDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public List<PermissionModel> getList() {
        String hql = "select p from PermissionModel p";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        List<PermissionModel> permissoins = (List<PermissionModel>) query.list();
        if (permissoins != null && !permissoins.isEmpty()) {
            return  permissoins;
        }
        return null;
    }

    @Override
    @Transactional
    public PermissionModel get(String resource, String action) {
        String hql = "from PermissionModel where resource = :resource and action = :action";
        Query query = sessionFactory.getCurrentSession().createQuery(hql).setString("resource", resource).setString("action", action);

        List<PermissionModel> permissions = (List<PermissionModel>) query.list();
        if (permissions != null && !permissions.isEmpty()) {
            return permissions.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    public PermissionModel get(long id) {
        String hql = "from PermissionModel where id = " + id;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        List<PermissionModel> permissions = (List<PermissionModel>) query.list();
        if (permissions != null && !permissions.isEmpty()) {
            return permissions.get(0);
        }
        return null;
    }


    @Override
    @Transactional
    public long insert(PermissionModel permission) {
        permission.setCreatedTs(DateTime.now());
        sessionFactory.getCurrentSession().save(permission);
        return permission.getId();
    }

    @Override
    @Transactional
    public List<PermissionModel> getPermissions(long groupId) {
        String hql = "from PermissionModel where groupId = " + groupId;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        List<PermissionModel> permissoins = (List<PermissionModel>) query.list();
        if (permissoins != null && !permissoins.isEmpty()) {
            return  permissoins;
        }
        return null;
    }

}
