package com.yunsoo.dao;

import com.yunsoo.dbmodel.UserModel;

import java.util.List;

/**
 * @author Zhe Zhang
 */
public interface UserDao {
    public UserModel get(Long id);

    public UserModel get(String cellular);

    public long save(UserModel userModel);

    public DaoStatus patchUpdate(UserModel userModelForPatch);

    public DaoStatus update(UserModel userModel);

    public DaoStatus delete(Long id, int deleteStatus);

    public List<UserModel> getAllUsers();

    public List<UserModel> getUsersByFilter(Long id, String deviceCode, String cellular, Integer status);
}