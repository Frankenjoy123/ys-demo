package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.BrandApplicationEntity;
import com.yunsoo.data.service.entity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by yan on 3/16/2016.
 */
public interface BrandApplicationRepository  extends CrudRepository<BrandApplicationEntity, String> {

    @Query("select be from BrandApplicationEntity be where (:carrierId is null or be.carrierId = :carrierId) and (:name is null or be.brandName = :name) ")
    Page<BrandApplicationEntity> query(@Param("name")String name, @Param("carrierId")String carrier_id, Pageable pageable);
}