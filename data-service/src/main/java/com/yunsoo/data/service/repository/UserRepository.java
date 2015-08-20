package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/8/20
 * Descriptions:
 */
public interface UserRepository extends FindOneAndSaveRepository<UserEntity, String> {

    List<UserEntity> findByDeviceId(String deviceId);

    List<UserEntity> findByPhone(String phone);

}
