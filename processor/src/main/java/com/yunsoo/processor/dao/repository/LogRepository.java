package com.yunsoo.processor.dao.repository;

import com.yunsoo.processor.dao.entity.LogEntity;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/**
 * Created by:   Lijian
 * Created on:   2016-04-29
 * Descriptions:
 */
public interface LogRepository extends Repository<LogEntity, String> {

    @Query("select o from #{#entityName} o where " +
            "(:eventName is null or o.eventName = :eventName) " +
            "and (:level is null or o.level = :level) " +
            "and (:identifier is null or o.identifier = :identifier) " +
            "and (:identifierName is null or o.identifierName = :identifierName) " +
            "and (:createdDateTimeGE is null or o.createdDateTime >= :createdDateTimeGE) " +
            "and (:createdDateTimeLE is null or o.createdDateTime <= :createdDateTimeLE)")
    Page<LogEntity> findByFilter(@Param("eventName") String eventName,
                                 @Param("level") String level,
                                 @Param("identifier") String identifier,
                                 @Param("identifierName") String identifierName,
                                 @Param("createdDateTimeGE") DateTime createdDateTimeGE,
                                 @Param("createdDateTimeLE") DateTime createdDateTimeLE,
                                 Pageable pageable);

    LogEntity save(LogEntity entity);

}
