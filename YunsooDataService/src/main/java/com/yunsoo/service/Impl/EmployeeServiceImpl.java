package com.yunsoo.service.Impl;

import com.yunsoo.dao.EmployeeDAO;
import com.yunsoo.model.Employee;
import com.yunsoo.service.EmployeeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zhe Zhang
 */
@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeDAO employeeDAO;

    @Override
    public void save(Employee employee){
        employeeDAO.save(employee);
    }

    @Override
    public void update(Employee employee) {
        employeeDAO.update(employee);
    }

    @Override
    public void delete(Employee employee){
       employeeDAO.delete(employee);
    }

    @Override
    @Transactional
    public List<Employee> getAllEmployees(){
        return employeeDAO.getAllEmployees();
    }
}