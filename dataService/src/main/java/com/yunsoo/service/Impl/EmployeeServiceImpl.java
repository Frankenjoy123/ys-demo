package com.yunsoo.service.Impl;

import com.yunsoo.dao.EmployeeDao;
import com.yunsoo.dbmodel.EmployeeModel;
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
    private EmployeeDao employeeDAO;

    @Override
    public void save(EmployeeModel employeeModel) {
        employeeDAO.save(employeeModel);
    }

    @Override
    public void update(EmployeeModel employeeModel) {
        employeeDAO.update(employeeModel);
    }

    @Override
    public void delete(EmployeeModel employeeModel) {
        employeeDAO.delete(employeeModel);
    }

    @Override
    @Transactional
    public List<EmployeeModel> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }
}