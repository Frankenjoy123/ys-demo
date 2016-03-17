package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MarketingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
public interface MarketingRepository extends CrudRepository<MarketingEntity, String> {
    Page<MarketingEntity> findByOrgId(String orgId, Pageable pageable);

    @Query("select m from MarketingEntity m where orgId in :orgIds and ( :status is null or statusCode = :status) order by createdDateTime desc")
    Page<MarketingEntity> query(@Param("orgIds")List<String> orgIds, @Param("status")String status, Pageable pageable);
}
