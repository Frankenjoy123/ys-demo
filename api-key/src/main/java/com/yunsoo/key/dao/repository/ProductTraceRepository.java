package com.yunsoo.key.dao.repository;

import com.yunsoo.key.dao.entity.ProductTraceEntity;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by:   yan
 * Created on:   10/13/2016
 * Descriptions:
 */
public interface ProductTraceRepository extends CrudRepository<ProductTraceEntity, String> {

    @Query("select sum(productCount) from ProductTraceEntity where sourceId=:sourceId " +
            "and sourceType=:sourceType and action=:action " +
            "and createdDateTime between :start and :end ")
    int sumProduct(@Param("sourceId") String sourceId, @Param("sourceType") String sourceType,
                   @Param("action") String action, @Param("start") DateTime start, @Param("end") DateTime end);

    List<ProductTraceEntity> findTop500ByStatusCode(String status);

    List<ProductTraceEntity> findByIdIn(List<String> ids);

    ProductTraceEntity findByProductKeyAndCreatedSourceIdAndCreatedSourceType(String key, String sourceId, String type);

}
