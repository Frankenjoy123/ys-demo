package com.yunsoo.service.Impl;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.UserDao;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.contract.User;
import com.yunsoo.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zhe Zhang
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDAO;

    @Override
    public User get(Long id) {
        return User.FromModel(userDAO.get(id));
    }

    @Override
    public User get(String cellular) {
        return User.FromModel(userDAO.get(cellular));
    }


    @Override
    public long save(User user) {
        if (user == null || user.getDeviceCode().isEmpty()) {
            return -1L;
        }
        return userDAO.save(User.ToModel(user));
    }

    @Override
    public ServiceOperationStatus update(User user) {
        if (user == null || user.getId().isEmpty()) {
            return ServiceOperationStatus.InvalidArgument;
        }
        DaoStatus daoStatus = userDAO.update(User.ToModel(user));
        if (daoStatus == DaoStatus.success) return ServiceOperationStatus.Success;
        else return ServiceOperationStatus.Fail;

    }

    @Override
    public boolean delete(Long id, int deleteStatus) {
        DaoStatus daoStatus = userDAO.delete(id, deleteStatus);
        if (daoStatus == DaoStatus.success) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return User.FromModelList(userDAO.getAllUsers());
    }

    @Override
    @Transactional
    public List<User> getUsersByFilter(Long id, String deviceCode, String cellular, Integer status) {
        return User.FromModelList(userDAO.getUsersByFilter(id, deviceCode, cellular, status));
    }
}