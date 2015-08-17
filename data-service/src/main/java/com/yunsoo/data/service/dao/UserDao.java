package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.UserModel;

import java.util.List;

/**
 * @author Zhe Zhang
 */
public interface UserDao {
    UserModel getById(String id);

    UserModel getByCellular(String cellular);

    String save(UserModel userModel);

    DaoStatus patchUpdate(UserModel userModelForPatch);

    DaoStatus update(UserModel userModel);

    DaoStatus delete(String id, String deleteStatus);

    List<UserModel> getAllUsers();

    List<UserModel> getUsersByFilter(String id, String deviceCode, String cellular, String status);

}