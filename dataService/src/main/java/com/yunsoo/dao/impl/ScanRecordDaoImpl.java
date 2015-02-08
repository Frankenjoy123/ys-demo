package com.yunsoo.dao.impl;

import com.yunsoo.dao.ScanRecordDao;
import com.yunsoo.dbmodel.ScanRecordModel;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Zhe on 2015/2/4.
 */
@Repository("scanRecordDao")
@Transactional
public class ScanRecordDaoImpl implements ScanRecordDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ScanRecordModel get(long id) {
        return (ScanRecordModel) sessionFactory.getCurrentSession().get(
                ScanRecordModel.class, id);
    }

    @Override
    public long save(ScanRecordModel scanRecordModel) {
        sessionFactory.getCurrentSession().save(scanRecordModel);
        return scanRecordModel.getId();
    }

    @Override
    public List<ScanRecordModel> getScanRecordsByFilter(String productKey, Integer baseProductId, Integer userId, DateTime createdDateTime, int pageIndex, int pageSize) {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(ScanRecordModel.class);
        if (!productKey.isEmpty()) {
            c.add(Restrictions.eq("productKey", productKey));
        }
        if (baseProductId != null) {
            c.add(Restrictions.eq("baseProductId", baseProductId.intValue()));
        }
        if (userId != null) {
            c.add(Restrictions.eq("userId", userId.intValue()));
        }
        if (createdDateTime != null) {
            c.add(Restrictions.eq("createdDateTime", createdDateTime));
        }
        c.setFirstResult(pageIndex * pageSize);
        c.setMaxResults(pageSize);
        return c.list();
    }
}