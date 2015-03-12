package com.yunsoo.dao.impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.LogisticsCheckPointDao;
import com.yunsoo.dbmodel.LogisticsCheckPointModel;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
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
    @Transactional
    public LogisticsCheckPointModel get(int id) {
        return (LogisticsCheckPointModel) sessionFactory.getCurrentSession().get(
                LogisticsCheckPointModel.class, id);
    }

    @Override
    public LogisticsCheckPointModel get(String name) {
        String hql = "from LogisticsCheckPointModel where name=" + name;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        @SuppressWarnings("unchecked")
        List<LogisticsCheckPointModel> listPoints = (List<LogisticsCheckPointModel>) query.list();
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
