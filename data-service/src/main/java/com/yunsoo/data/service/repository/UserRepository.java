package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductBaseEntity;
import com.yunsoo.data.service.entity.UserEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
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

    Page<UserEntity> findByOauthOpenidAndOauthTypeCode(String openId, String oauthType, Pageable pageable);

    Page<UserEntity> findByDeviceId(String deviceId, Pageable pageable);

    Page<UserEntity> findByPhone(String phone, Pageable pageable);

    Page<UserEntity> findByIdIn(List<String> Ids, Pageable pageable);

    @Query("select o from #{#entityName} o where " +
            "(:pointGE is null or o.point >= :pointGE) " +
            "and (:pointLE is null or o.point <= :pointLE)")
    Page<UserEntity> query(@Param("pointGE") Integer pointGE, @Param("pointLE") Integer pointLE, Pageable pageable);

    @Query("select o from #{#entityName} o where (:sex is null or o.sex = :sex) and (:phone is null or :phone = '' or o.phone like ('%' || :phone || '%'))  " +
            "and (:name is null or :name = '' or o.name like ('%' || :name || '%')) " +
            "and (:province is null or :province = '' or o.address like ('%' || :province || '%')) " +
            "and (:city is null or :city = '' or o.address like ('%' || :city || '%')) " +
            "and ((:ageStart is null or (o.age >= :ageStart)) and (:ageEnd is null or (o.age <= :ageEnd))) " +
            "and (:createdDateTimeStart is null or o.createdDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or o.createdDateTime <= :createdDateTimeEnd)")
    Page<UserEntity> findByFilter(@Param("sex") Boolean sex, @Param("phone") String phone, @Param("name") String name,
                                  @Param("province") String province, @Param("city") String city,
                                  @Param("ageStart") Integer ageStart, @Param("ageEnd") Integer ageEnd,
                                  @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                                  @Param("createdDateTimeEnd") DateTime createdDateTimeEnd, Pageable pageable);
}
