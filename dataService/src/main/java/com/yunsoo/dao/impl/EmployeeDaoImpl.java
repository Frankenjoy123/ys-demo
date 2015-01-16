package com.yunsoo.dao.impl;

import com.yunsoo.dao.EmployeeDao;
import com.yunsoo.dbmodel.EmployeeModel;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Zhe Zhang
 */
@Repository("employeeDAO")
public class EmployeeDaoImpl implements EmployeeDao{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(EmployeeModel employeeModel) {
        sessionFactory.getCurrentSession().save(employeeModel);
    }

    @Override
    public void update(EmployeeModel employeeModel) {
        sessionFactory.getCurrentSession().update(employeeModel);
    }

    @Override
    public void delete(EmployeeModel employeeModel) {
        sessionFactory.getCurrentSession().delete(employeeModel);
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<EmployeeModel> getAllEmployees() {
        return sessionFactory.getCurrentSession().createCriteria(EmployeeModel.class).list();
    }
}
