package com.yunsoo.data.service.service;

import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.data.service.service.contract.User;

import java.io.IOException;
import java.util.List;

/**
 * @author Zhe Zhang
 */
public interface UserService {

    public User getById(String id);

    public User getByCellular(String cellular);

    public S3Object getUserThumbnail(String bucket, String key) throws IOException;

    public String save(User user) throws Exception;

    public ServiceOperationStatus update(User user) throws Exception;

    public ServiceOperationStatus patchUpdate(User user) throws Exception;

    public boolean delete(String id);

    public List<User> getAllUsers();

    public List<User> getUsersByFilter(String id, String deviceCode, String cellular, String status);

}
