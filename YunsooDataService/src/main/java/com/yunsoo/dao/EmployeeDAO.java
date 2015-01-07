package com.yunsoo.dao;

import com.yunsoo.model.Employee;
import java.util.List;

/**
 * @author Zhe Zhang
 */
public interface EmployeeDAO{
    public void save(Employee employee);
    public void update(Employee employee);
    public void delete(Employee employee);
    public List<Employee> getAllEmployees();
}