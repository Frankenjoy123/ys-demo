package com.yunsoo.dao.impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.DeviceDao;
import com.yunsoo.dbmodel.DeviceModel;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Jerry on 3/13/2015.
 */
@Repository("deviceDao")
@Transactional
public class DeviceDaoImpl implements DeviceDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public DeviceModel get(long id) {
        return (DeviceModel) sessionFactory.getCurrentSession().get(
                DeviceModel.class, id);
    }

    @Override
    public long save(DeviceModel deviceModel) {
        sessionFactory.getCurrentSession().save(deviceModel);
        return deviceModel.getId();
    }

    @Override
    public DaoStatus update(DeviceModel deviceModel) {
        try {
            sessionFactory.getCurrentSession().update(deviceModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log ex
            return DaoStatus.fail;
        }
    }

    @Override
    public DaoStatus delete(long Id, int deleteStatus) {
        try {
            DeviceModel deviceModel = this.get(Id);
            if (deviceModel == null) return DaoStatus.NotFound;

            sessionFactory.getCurrentSession().delete(deviceModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log ex
            return DaoStatus.fail;
        }
    }

}
