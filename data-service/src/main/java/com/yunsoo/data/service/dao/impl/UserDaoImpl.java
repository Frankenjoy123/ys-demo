package com.yunsoo.data.service.dao.impl;

import com.yunsoo.data.service.dao.DaoStatus;
import com.yunsoo.data.service.dao.UserDao;
import com.yunsoo.data.service.dbmodel.UserModel;

import java.util.List;

import com.yunsoo.data.service.entity.UserFollowingEntity;
import com.yunsoo.data.service.repository.UserFollowingRepository;
import com.yunsoo.data.service.service.contract.UserFollowing;
import com.yunsoo.data.service.util.SpringBeanUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zhe Zhang
 */
@Repository("userDAO")
@Transactional
public class UserDaoImpl implements UserDao {

    @Autowired
    @Qualifier(value = "sessionfactory.primary")
    private SessionFactory sessionFactory;

    @Autowired
    @Qualifier(value = "sessionfactory.read1")
    private SessionFactory readSessionFactory;

    @Override
    @Transactional
    public UserModel getById(String id) {
        return (UserModel) readSessionFactory.getCurrentSession().get(
                UserModel.class, id);
    }

    @Override
    public UserModel getByCellular(String cellular) {
        List<UserModel> listUser = this.getUsersByFilter(null, null, cellular, null);
        if (listUser != null && !listUser.isEmpty()) {
            return listUser.get(0);
        }
        return null;
    }

    @Override
    public String save(UserModel userModel) {
        userModel.setCreatedDateTime(DateTime.now());
        sessionFactory.getCurrentSession().save(userModel);
        return userModel.getId();
    }

    //Dynamic update model for intended updated properties.
    @Override
    public DaoStatus patchUpdate(UserModel userModelForPatch) {
        try {
            Session currentSession = sessionFactory.getCurrentSession();
            UserModel modelInDB = (UserModel) currentSession.get(UserModel.class, userModelForPatch.getId());
            if (modelInDB == null) {
                return DaoStatus.NotFound;
            }
            //Set properties that needs to update in DB, ignore others are null.
            BeanUtils.copyProperties(userModelForPatch, modelInDB, SpringBeanUtil.getNullPropertyNames(userModelForPatch));
            currentSession.update(modelInDB);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log
            System.out.println(ex.getStackTrace());
            System.out.println(ex.getMessage());
            return DaoStatus.fail;
        }
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
    public DaoStatus delete(String Id, String deleteStatus) {
//        sessionFactory.getCurrentSession().deletePermanantly(userModel);
        return updateStatus(Id, deleteStatus);
    }

    public DaoStatus updateStatus(String userId, String status) {
        UserModel userModel = this.getById(userId);
        if (userModel == null) return DaoStatus.NotFound;

        userModel.setStatus(status); //find in config file for deleted status
        this.update(userModel);
        return DaoStatus.success;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UserModel> getAllUsers() {
        return readSessionFactory.getCurrentSession().createCriteria(UserModel.class).list();
    }

    @Override
    public List<UserModel> getUsersByFilter(String id, String deviceCode, String cellular, String status) {
        Criteria c = readSessionFactory.getCurrentSession().createCriteria(UserModel.class);
        if (id != null && !id.isEmpty()) {
            c.add(Restrictions.eq("id", id));
        }
        if (deviceCode != null && !deviceCode.isEmpty()) {
            c.add(Restrictions.eq("deviceCode", deviceCode));
        }
        if (cellular != null && !cellular.isEmpty()) {
            c.add(Restrictions.eq("cellular", cellular));
        }
        if (status != null && !status.isEmpty()) {
            c.add(Restrictions.eq("statusId", status));
        }
        return c.list();
    }

}
