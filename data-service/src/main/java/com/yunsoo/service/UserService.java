package com.yunsoo.service;

import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.model.ThumbnailFile;
import com.yunsoo.service.contract.User;

import java.io.IOException;
import java.util.List;

/**
 * @author Zhe Zhang
 */
public interface UserService {

    public User get(Long id);

    public User get(String cellular);

    public S3Object getUserThumbnail(String bucket, String key) throws IOException;

    public long save(User user) throws Exception;

    public ServiceOperationStatus update(User user) throws Exception;

    public ServiceOperationStatus patchUpdate(User user) throws Exception;

    public boolean delete(Long id);

    public List<User> getAllUsers();

    public List<User> getUsersByFilter(Long id, String deviceCode, String cellular, Integer status);

}