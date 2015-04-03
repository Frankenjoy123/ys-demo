package com.yunsoo.dao.impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.LogisticsCheckActionDao;
import com.yunsoo.dbmodel.LogisticsCheckActionModel;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Chen Jerry on 3/3/2015.
 */
@Repository("logisticsCheckActionDao")
@Transactional
public class LogisticsCheckActionDaoImpl implements LogisticsCheckActionDao{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public LogisticsCheckActionModel get(int id) {
        return (LogisticsCheckActionModel) sessionFactory.getCurrentSession().get(
                LogisticsCheckActionModel.class, id);
    }

    @Override
    public LogisticsCheckActionModel get(String name) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(LogisticsCheckActionModel.class);
        c.add(Restrictions.eq("name", name));
        List<LogisticsCheckActionModel> listActions = (List<LogisticsCheckActionModel>) c.list();
        if (listActions != null && !listActions.isEmpty()) {
            return listActions.get(0);
        }
        return null;
    }

    @Override
    public int save(LogisticsCheckActionModel actionModel) {
        sessionFactory.getCurrentSession().save(actionModel);
        return actionModel.getId();
    }

    @Override
    public DaoStatus update(LogisticsCheckActionModel actionModel) {
        try {
            sessionFactory.getCurrentSession().update(actionModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log ex
            return DaoStatus.fail;
        }
    }

    @Override
    public DaoStatus delete(int Id, int deleteStatus) {
        try {
            LogisticsCheckActionModel actionModel = this.get(Id);
            if (actionModel == null) return DaoStatus.NotFound;

            sessionFactory.getCurrentSession().delete(actionModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log ex
            return DaoStatus.fail;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LogisticsCheckActionModel> getAllLogisticsCheckActionModels() {
        return sessionFactory.getCurrentSession().createCriteria(LogisticsCheckActionModel.class).list();
    }

    @Override
    public List<LogisticsCheckActionModel> getLogisticsCheckActionModelsByFilter(int id, String name){
        Criteria c = sessionFactory.getCurrentSession().createCriteria(LogisticsCheckActionModel.class);
        if (id >= 0) {
            c.add(Restrictions.eq("id", id));
        }
        if (!name.isEmpty()) {
            c.add(Restrictions.eq("name", name));
        }
        return c.list();
    }

    @Override
    public List<LogisticsCheckActionModel> getLogisticsCheckActionModelsByOrg(int orgId)
    {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(LogisticsCheckActionModel.class);
        c.add(Restrictions.eq("orgId", orgId));

        return c.list();
    }
}
