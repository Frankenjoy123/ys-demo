package com.yunsoo.data.service.dao.impl;

import com.yunsoo.data.service.dao.UserOrganizationDao;
import com.yunsoo.data.service.dao.DaoStatus;
import com.yunsoo.data.service.dbmodel.UserFollowingModel;
import com.yunsoo.data.service.util.SpringBeanUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Zhe on 2015/3/24.
 */
public class UserOrganizationDaoImpl implements UserOrganizationDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public UserFollowingModel get(String userId, String orgId) {
        List<UserFollowingModel> model = this.getUserOrgModelByFilter(userId, orgId, false);
        return model.get(0);
    }

    @Override
    public List<UserFollowingModel> getUserOrgModelByFilter(String userId, String organizationId, Boolean isFollowing) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(UserFollowingModel.class);
        if (userId != null && !userId.isEmpty()) {
            c.add(Restrictions.eq("userId", userId));
        }
        if (organizationId != null && !organizationId.isEmpty()) {
            c.add(Restrictions.eq("organizationId", organizationId));
        }
        if (!isFollowing) {
            c.add(Restrictions.gt("isFollowing", isFollowing));
        }
        return c.list();
    }

    @Override
    public Long save(UserFollowingModel model) {
        model.setCreatedDateTime(DateTime.now());
        sessionFactory.getCurrentSession().save(model);
        return model.getId();
    }

    @Override
    public DaoStatus patchUpdate(UserFollowingModel messageModelForPatch) {
        try {
            Session currentSession = sessionFactory.getCurrentSession();
            UserFollowingModel modelInDB = (UserFollowingModel) currentSession.get(UserFollowingModel.class, messageModelForPatch.getId());
            //Set properties that needs to update in DB, ignore others are null.
            BeanUtils.copyProperties(messageModelForPatch, modelInDB, SpringBeanUtil.getNullPropertyNames(messageModelForPatch));
            currentSession.update(modelInDB);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log
            System.out.println(ex.getStackTrace());
            System.out.println(ex.getMessage());
            return DaoStatus.fail;
        }
    }
}
