package com.yunsoo.data.service.service;

import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.data.service.service.contract.User;

import java.io.IOException;
import java.util.List;

/**
 * @author Zhe Zhang
 */
public interface UserService {

    User getById(String id);

    User getByCellular(String cellular);

    S3Object getUserThumbnail(String bucket, String key) throws IOException;

    String save(User user) throws Exception;

    ServiceOperationStatus update(User user) throws Exception;

    ServiceOperationStatus patchUpdate(User user) throws Exception;

    void delete(String id);

    List<User> getAllUsers();

    List<User> getUsersByFilter(String id, String deviceCode, String cellular, String status);

}
