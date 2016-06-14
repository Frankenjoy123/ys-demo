package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MktDrawRuleEntity;
import com.yunsoo.data.service.entity.UserTagEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Admin on 6/13/2016.
 */
public interface UserTagRepository extends FindOneAndSaveRepository<UserTagEntity, String> {

    @Query("select o from #{#entityName} o where (:userId is null or :userId = '' or o.userId = :userId)  " +
            "and (:ysId is null or :ysId = '' or o.ysId = :ysId) " +
            "and (:orgId is null or :orgId = '' or o.orgId = :orgId) order by createdDateTime DESC")
    List<UserTagEntity> findByFilter(@Param("userId") String userId, @Param("ysId") String ysId, @Param("orgId") String orgId);

    @Transactional
    void deleteByOrgIdAndYsIdAndUserId(String orgId, String ysId, String userId);

    List<UserTagEntity> save(Iterable<UserTagEntity> entities);
}
