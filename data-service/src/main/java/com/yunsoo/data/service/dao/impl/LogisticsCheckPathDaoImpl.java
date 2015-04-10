package com.yunsoo.data.service.dao.impl;

import com.yunsoo.data.service.dao.DaoStatus;
import com.yunsoo.data.service.dao.LogisticsCheckPathDao;
import com.yunsoo.data.service.dbmodel.LogisticsCheckPathModel;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Chen Jerry on 3/4/2015.
 */
@Repository("logisticsCheckPathDao")
@Transactional
public class LogisticsCheckPathDaoImpl implements LogisticsCheckPathDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public LogisticsCheckPathModel get(long id) {
        return (LogisticsCheckPathModel) sessionFactory.getCurrentSession().get(
                LogisticsCheckPathModel.class, id);
    }

    @Override
    public Long save(LogisticsCheckPathModel pathModel) {
        try
        {
        sessionFactory.getCurrentSession().save(pathModel);
        return pathModel.getId();

        } catch (Exception ex) {
            //log ex
           return -1l;
        }
    }

    @Override
    public DaoStatus update(LogisticsCheckPathModel pathModel) {
        try {
            sessionFactory.getCurrentSession().update(pathModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log ex
            return DaoStatus.fail;
        }
    }

    @Override
    public DaoStatus delete(long Id, int deleteStatus) {
        try {
            LogisticsCheckPathModel pathModel = this.get(Id);
            if (pathModel == null) return DaoStatus.NotFound;

            sessionFactory.getCurrentSession().delete(pathModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log ex
            return DaoStatus.fail;
        }
    }

    @Override
    public List<LogisticsCheckPathModel> getLogisticsCheckPathModelsOrderByStartDate(String productKey)
    {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(LogisticsCheckPathModel.class);

        if (productKey!=null && !productKey.isEmpty()) {
            c.add(Restrictions.eq("productKey", productKey))
             .addOrder(Order.asc("startDate"));
        }

        return c.list();
    }
}
