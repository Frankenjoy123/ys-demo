package com.yunsoo.dao.impl;

import com.yunsoo.dao.EmployeeDao;
import com.yunsoo.model.Employee;

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
    public void save(Employee employee) {
        sessionFactory.getCurrentSession().save(employee);
    }

    @Override
    public void update(Employee employee) {
        sessionFactory.getCurrentSession().update(employee);
    }

    @Override
    public void delete(Employee employee) {
        sessionFactory.getCurrentSession().delete(employee);
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<Employee> getAllEmployees() {
       return sessionFactory.getCurrentSession().createCriteria(Employee.class).list();
    }
}
