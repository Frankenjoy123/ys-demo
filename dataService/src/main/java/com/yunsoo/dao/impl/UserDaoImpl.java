package com.yunsoo.dao.impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.UserDao;
import com.yunsoo.dbmodel.UserModel;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zhe Zhang
 */
@Repository("userDAO")
@Transactional
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public UserModel get(Long id) {
        String hql = "from UserModel where id=" + id;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        @SuppressWarnings("unchecked")
        List<UserModel> listUser = (List<UserModel>) query.list();
        if (listUser != null && !listUser.isEmpty()) {
            return listUser.get(0);
        }
        return null;
    }

    @Override
    public long save(UserModel userModel) {
        sessionFactory.getCurrentSession().save(userModel);
        return userModel.getId();
    }

    @Override
    public DaoStatus update(UserModel userModel) {
        try {
            sessionFactory.getCurrentSession().update(userModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log ex
            return DaoStatus.fail;
        }
    }

    @Override
    public DaoStatus delete(Long Id, int deleteStatus) {
//        sessionFactory.getCurrentSession().delete(userModel);
        return updateStatus(Id, deleteStatus);
    }

    public DaoStatus updateStatus(Long userId, int status) {
        UserModel userModel = this.get(userId);
        if (userModel == null) return DaoStatus.NotFound;

        userModel.setStatusId(status); //find in config file for deleted status
        this.update(userModel);
        return DaoStatus.success;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UserModel> getAllUsers() {
        return sessionFactory.getCurrentSession().createCriteria(UserModel.class).list();
    }

    @Override
    public List<UserModel> getUsersByFilter(Long id, String deviceCode, String cellular, Integer status) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(UserModel.class);
        if (id != null) {
            c.add(Restrictions.eq("id", id.longValue()));
        }
        if (!deviceCode.isEmpty()) {
            c.add(Restrictions.eq("deviceCode", deviceCode));
        }
        if (!cellular.isEmpty()) {
            c.add(Restrictions.eq("cellular", cellular));
        }
        if (status != null) {
            c.add(Restrictions.eq("statusId", status.intValue()));
        }
        return c.list();
    }

}