package com.yunsoo.dao.impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.LogisticsCheckPathDao;
import com.yunsoo.dbmodel.LogisticsCheckActionModel;
import com.yunsoo.dbmodel.LogisticsCheckPathModel;
import org.hibernate.Criteria;
import org.hibernate.Query;
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
    public LogisticsCheckPathModel get(Long id) {
        String hql = "from LogisticsCheckPathModel where Id=" + id;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        @SuppressWarnings("unchecked")
        List<LogisticsCheckPathModel> listPaths = (List<LogisticsCheckPathModel>) query.list();
        if (listPaths != null && !listPaths.isEmpty()) {
            return listPaths.get(0);
        }
        return null;
    }

    @Override
    public Long save(LogisticsCheckPathModel pathModel) {
        sessionFactory.getCurrentSession().save(pathModel);
        return pathModel.getId();
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
    public DaoStatus delete(Long Id, int deleteStatus) {
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
