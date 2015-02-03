package com.yunsoo.service;

import com.yunsoo.dbmodel.UserModel;
import com.yunsoo.service.contract.User;

import java.util.List;

/**
 * @author Zhe Zhang
 */
public interface UserService {

    public User get(Long id);

    public long save(User user);

    public ServiceOperationStatus update(User user);

    public boolean delete(Long id, int deleteStatus);

    public List<User> getAllUsers();

    public List<User> getUsersByFilter(Long id, String deviceCode, String cellular, Integer status);

}
