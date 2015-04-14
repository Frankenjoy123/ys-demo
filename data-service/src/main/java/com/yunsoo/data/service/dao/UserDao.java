package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.UserModel;

import java.util.List;

/**
 * @author Zhe Zhang
 */
public interface UserDao {
    public UserModel get(String id);

    public UserModel getByCellular(String cellular);

    public String save(UserModel userModel);

    public DaoStatus patchUpdate(UserModel userModelForPatch);

    public DaoStatus update(UserModel userModel);

    public DaoStatus delete(String id, int deleteStatus);

    public List<UserModel> getAllUsers();

    public List<UserModel> getUsersByFilter(String id, String deviceCode, String cellular, Integer status);
}