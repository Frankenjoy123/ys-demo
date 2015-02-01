package com.yunsoo.service;

import com.yunsoo.service.contract.User;

import java.util.List;

/**
 * @author Zhe Zhang
 */
public interface UserService {

    public User get(int id);

    public void save(User user);

    public void update(User user);

    public void delete(User user);

    public List<User> getAllUsers();


}
