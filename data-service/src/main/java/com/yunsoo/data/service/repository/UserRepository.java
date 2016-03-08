package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/8/20
 * Descriptions:
 */
public interface UserRepository extends FindOneAndSaveRepository<UserEntity, String> {

    List<UserEntity> findByPhone(String phone);

    Page<UserEntity> findByOauthOpenid(String openId, Pageable pageable);

    Page<UserEntity> findByDeviceId(String deviceId, Pageable pageable);

    Page<UserEntity> findByPhone(String phone, Pageable pageable);

    Page<UserEntity> findByIdIn(List<String> Ids, Pageable pageable);

    @Query("select o from #{#entityName} o where " +
            "(:pointGE is null or o.point >= :pointGE) " +
            "and (:pointLE is null or o.point <= :pointLE)")
    Page<UserEntity> query(@Param("pointGE") Integer pointGE, @Param("pointLE") Integer pointLE, Pageable pageable);

}
