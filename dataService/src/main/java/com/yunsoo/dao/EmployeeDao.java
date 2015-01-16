package com.yunsoo.dao;

import com.yunsoo.dbmodel.EmployeeModel;

import java.util.List;

/**
 * @author Zhe Zhang
 */
public interface EmployeeDao{
    public void save(EmployeeModel employeeModel);

    public void update(EmployeeModel employeeModel);

    public void delete(EmployeeModel employeeModel);

    public List<EmployeeModel> getAllEmployees();
}