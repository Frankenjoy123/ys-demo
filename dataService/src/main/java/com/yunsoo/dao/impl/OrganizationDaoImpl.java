package com.yunsoo.dao.impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.OrganizationDao;
import com.yunsoo.dbmodel.OrganizationModel;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Chen Jerry on 3/12/2015.
 */
@Repository("organizationDao")
@Transactional
public class OrganizationDaoImpl implements OrganizationDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public OrganizationModel get(long id) {
        return (OrganizationModel) sessionFactory.getCurrentSession().get(
                OrganizationModel.class, id);
    }

    @Override
    public OrganizationModel get(String name) {
        String hql = "from OrganizationModel where name=" + name;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        @SuppressWarnings("unchecked")
        List<OrganizationModel> orgs = (List<OrganizationModel>) query.list();
        if (orgs != null && !orgs.isEmpty()) {
            return orgs.get(0);
        }
        return null;
    }

    @Override
    public long save(OrganizationModel orgModel) {
        sessionFactory.getCurrentSession().save(orgModel);
        return orgModel.getId();
    }

    @Override
    public DaoStatus update(OrganizationModel orgModel) {
        try {
            sessionFactory.getCurrentSession().update(orgModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log ex
            return DaoStatus.fail;
        }
    }

    @Override
    public DaoStatus delete(long Id, int deleteStatus) {
        try {
            OrganizationModel orgModel = this.get(Id);
            if (orgModel == null) return DaoStatus.NotFound;

            sessionFactory.getCurrentSession().delete(orgModel);
            return DaoStatus.success;
        } catch (Exception ex) {
            //log ex
            return DaoStatus.fail;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OrganizationModel> getAllOrganizationModelModels() {
        return sessionFactory.getCurrentSession().createCriteria(OrganizationModel.class).list();
    }
}
