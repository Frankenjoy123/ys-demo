/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunsoo.dao.impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.LogisticsDao;
import com.yunsoo.dbmodel.LogisticsCheckPathModel;
import com.yunsoo.dbmodel.LogisticsCheckPointModel;
import com.yunsoo.dbmodel.LogisticsKeyTrackingModel;
import com.yunsoo.dbmodel.LogisticsTrackingModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author qyu
 */
@Repository("logisticsDao")
@Transactional
public class LogisticsDaoImpl implements LogisticsDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public LogisticsTrackingModel getTrackingInfo(String key) {
        Criteria cr = sessionFactory.getCurrentSession().createCriteria(LogisticsKeyTrackingModel.class);
        cr.add(Restrictions.eq("key", key));
        List result = cr.list();
        if (result == null || result.isEmpty()) {
            return null;
        }

        LogisticsKeyTrackingModel keyTracking = (LogisticsKeyTrackingModel) result.get(0);

        return keyTracking.getLogisticsInfo();

    }

    @Override
    public long createTracking(LogisticsTrackingModel model) {

        model.setStatus_id(0);
        sessionFactory.getCurrentSession().save(model);
        return model.getId();
    }

    @Override
    public DaoStatus updateTracking(LogisticsTrackingModel model) {
        sessionFactory.getCurrentSession().save(model);
        return DaoStatus.success;
    }

    @Override
    public DaoStatus deleteTracking(LogisticsTrackingModel model) {
        model.setStatus_id(2);//2 delete
        return updateTracking(model);
    }

    @Override
    public LogisticsCheckPointModel getPoint(int id) {
        LogisticsCheckPointModel point = (LogisticsCheckPointModel) sessionFactory.getCurrentSession().get(LogisticsCheckPointModel.class, id);
        return point;
    }

    @Override
    public DaoStatus createPoint(LogisticsCheckPointModel model) {
        sessionFactory.getCurrentSession().save(model);
        return DaoStatus.success;
    }

    @Override
    public DaoStatus updatePoint(LogisticsCheckPointModel model) {
        sessionFactory.getCurrentSession().save(model);
        return DaoStatus.success;
    }

    @Override
    public DaoStatus deletePoint(LogisticsCheckPointModel model) {
        sessionFactory.getCurrentSession().delete(model);
        return DaoStatus.success;
    }

    @Override
    public List<LogisticsCheckPointModel> getPointsByOrgId(int org_id) {
        Criteria cr = sessionFactory.getCurrentSession().createCriteria(LogisticsCheckPathModel.class);
        cr.add(Restrictions.eq("org_id", org_id));
        List<LogisticsCheckPointModel> points = new ArrayList<>();
        List list = cr.list();
        for (Iterator it = list.iterator(); it.hasNext();) {
            LogisticsCheckPointModel object = (LogisticsCheckPointModel) it.next();
            points.add(object);
        }
        return points;
    }

    @Override
    public LogisticsCheckPathModel getPath(long id) {
        LogisticsCheckPathModel model = (LogisticsCheckPathModel) sessionFactory.getCurrentSession().get(LogisticsCheckPathModel.class, id);
        return model;
    }

    @Override
    public DaoStatus createPath(LogisticsCheckPathModel model) {
        sessionFactory.getCurrentSession().save(model);
        return DaoStatus.success;
    }

    @Override
    public DaoStatus updatePath(LogisticsCheckPathModel model) {
        sessionFactory.getCurrentSession().save(model);
        return DaoStatus.success;
    }

    @Override
    public DaoStatus deletePath(LogisticsCheckPathModel model) {
        model.setStatus_id(2);//2 delete
        return updatePath(model);
    }

    @Override
    public List<LogisticsCheckPathModel> getPathsByTrackingId(long trackingId) {
        Criteria cr = sessionFactory.getCurrentSession().createCriteria(LogisticsCheckPathModel.class);
        Criterion orgCriterion = Restrictions.eq("tracking_id", trackingId);
        Criterion statusCriterion = Restrictions.ne("status_id", 2);
        LogicalExpression expression = Restrictions.and(orgCriterion, statusCriterion);
        cr.add(expression);
        List list = cr.list();
        List<LogisticsCheckPathModel> paths = new ArrayList<>();
        for (Iterator it = list.iterator(); it.hasNext();) {
            LogisticsCheckPathModel object = (LogisticsCheckPathModel) it.next();
            paths.add(object);
        }
        return paths;
    }

}
