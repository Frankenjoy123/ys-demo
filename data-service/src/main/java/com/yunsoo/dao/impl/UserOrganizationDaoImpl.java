package com.yunsoo.dao.impl;

import com.yunsoo.config.DataServiceSetting;
import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.MessageDao;
import com.yunsoo.dao.UserOrganizationDao;
import com.yunsoo.dbmodel.UserOrganizationModel;
import com.yunsoo.util.SpringBeanUtil;
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

    @Autowired
    private DataServiceSetting dataServiceSetting;

    @Override
    public UserOrganizationModel get(long userId, long companyId) {
        List<UserOrganizationModel> model = this.getUserOrgModelByFilter(userId, companyId, false);
        return model.get(0);
    }

    @Override
    public List<UserOrganizationModel> getUserOrgModelByFilter(Long userId, Long organizationId, Boolean isFollowing) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(UserOrganizationModel.class);
        if (userId != null) {
            c.add(Restrictions.eq("userId", userId.longValue()));
        }
        if (organizationId != null) {
            c.add(Restrictions.eq("organizationId", organizationId.longValue()));
        }
        if (!isFollowing) {
            c.add(Restrictions.gt("isFollowing", isFollowing));
        }
        return c.list();
    }

    @Override
    public Long save(UserOrganizationModel model) {
        model.setCreatedDateTime(DateTime.now());
        sessionFactory.getCurrentSession().save(model);
        return model.getId();
    }

    @Override
    public DaoStatus patchUpdate(UserOrganizationModel messageModelForPatch) {
        try {
            Session currentSession = sessionFactory.getCurrentSession();
            UserOrganizationModel modelInDB = (UserOrganizationModel) currentSession.get(UserOrganizationModel.class, messageModelForPatch.getId());
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
