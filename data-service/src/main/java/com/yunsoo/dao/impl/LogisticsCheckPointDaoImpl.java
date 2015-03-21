package com.yunsoo.dao.impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.LogisticsCheckPointDao;
import com.yunsoo.dbmodel.LogisticsCheckPointModel;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
@Repository("logisticsCheckPointDao")
@Transactional
public class LogisticsCheckPointDaoImpl implements LogisticsCheckPointDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public LogisticsCheckPointModel get(int id) {
        return (LogisticsCheckPointModel) sessionFactory.getCurrentSession().get(
                LogisticsCheckPointModel.class, id);
    }

    @Override
    public LogisticsCheckPointModel get(String name) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(LogisticsCheckPointModel.class);
        c.add(Restrictions.eq("name", name));
        List<LogisticsCheckPointModel> listPoints = (List<LogisticsCheckPointModel>) c.list();
        if (listPoints != null && !listPoints.isEmpty()) {
            return listPoints.get(0);
        }
        return null;
    }

    @Override
    public int save(LogisticsCheckPointModel pointModel) {
        sessionFactory.getCurrentSession().save(pointModel);
        return pointModel.getId();
    }

    @Override
    public DaoStatus update(LogisticsCheckPointModel pointModel) {
        try {
            sessionFactory.getCurrentSession().update(pointModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log ex
            return DaoStatus.fail;
        }
    }

    @Override
    public DaoStatus delete(int Id, int deleteStatus) {
        try {
            LogisticsCheckPointModel pointModel = this.get(Id);
            if (pointModel == null) return DaoStatus.NotFound;

            sessionFactory.getCurrentSession().delete(pointModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log ex
            return DaoStatus.fail;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LogisticsCheckPointModel> getAllLogisticsCheckPointModels() {
        return sessionFactory.getCurrentSession().createCriteria(LogisticsCheckPointModel.class).list();
    }
}
