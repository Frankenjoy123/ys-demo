package com.yunsoo.dao;

import com.yunsoo.dbmodel.UserModel;

import java.util.List;

/**
 * @author Zhe Zhang
 */
public interface UserDao {
    public UserModel get(int id);

    public void save(UserModel userModel);

    public void update(UserModel userModel);

    public void delete(UserModel userModel);

    public List<UserModel> getAllUsers();
}