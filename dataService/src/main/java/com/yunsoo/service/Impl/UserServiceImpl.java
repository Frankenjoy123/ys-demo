package com.yunsoo.service.Impl;

import com.yunsoo.dao.UserDao;
import com.yunsoo.service.contract.User;
import com.yunsoo.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.soap.SOAPBinding;

/**
 * @author Zhe Zhang
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDAO;

    @Override
    public User get(int id) {
        return User.FromModel(userDAO.get(id));
    }

    @Override
    public void save(User user) {
        userDAO.save(User.ToModel(user));
    }

    @Override
    public void update(User user) {
        userDAO.update(User.ToModel(user));
    }

    @Override
    public void delete(User user) {
        userDAO.delete(User.ToModel(user));
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return User.FromModelList(userDAO.getAllUsers());
    }

}