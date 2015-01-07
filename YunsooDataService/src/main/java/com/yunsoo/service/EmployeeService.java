package com.yunsoo.service;

import com.yunsoo.model.Employee;
import java.util.List;

/**
 * @author Zhe Zhang
 */
public interface EmployeeService {
    public void save(Employee employee);
    public void update(Employee employee);
    public void delete(Employee employee);
    public List<Employee> getAllEmployees();
}
