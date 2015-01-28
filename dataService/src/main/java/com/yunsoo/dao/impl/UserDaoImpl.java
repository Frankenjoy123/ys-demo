package com.yunsoo.dao.impl;

import com.yunsoo.dao.UserDao;
import com.yunsoo.dbmodel.UserModel;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zhe Zhang
 */
@Repository("userDAO")
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public UserModel get(int id) {
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
    public void save(UserModel userModel) {
        sessionFactory.getCurrentSession().save(userModel);
    }

    @Override
    public void update(UserModel userModel) {
        sessionFactory.getCurrentSession().update(userModel);
    }

    @Override
    public void delete(UserModel userModel) {
        sessionFactory.getCurrentSession().delete(userModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UserModel> getAllUsers() {
        return sessionFactory.getCurrentSession().createCriteria(UserModel.class).list();
    }
}
