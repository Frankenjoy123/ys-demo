package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserBlockEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserBlockRepository extends FindOneAndSaveRepository<UserBlockEntity, String> {

    List<UserBlockEntity> findByUserIdAndOrgId(String userId, String orgId);

    List<UserBlockEntity> findByYsIdAndOrgId(String ysId, String orgId);

    void delete(String id);

    @Query("select o from #{#entityName} o where (:userId is null or :userId = '' or o.userId = :userId)  " +
            "and (:ysId is null or :ysId = '' or o.ysId = :ysId) " +
            "and (:orgId is null or :orgId = '' or o.orgId = :orgId)")
    List<UserBlockEntity> findByFilter(@Param("userId") String userId, @Param("ysId") String ysId, @Param("orgId") String orgId);
}
