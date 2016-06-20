package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.TaskFileEntryEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-05-31
 * Descriptions:
 */
public interface TaskFileEntryRepository extends FindOneAndSaveRepository<TaskFileEntryEntity, String> {

    @Query("select e from #{#entityName} e " +
            "where (:orgId is null or e.orgId = :orgId) " +
            "and (:appId is null or e.appId = :appId) " +
            "and (:deviceId is null or e.deviceId = :deviceId) " +
            "and (:typeCode is null or e.typeCode = :typeCode) " +
            "and (e.statusCode in (:statusCodeIn) or :statusCodeInIgnored = true) " +
            "and (:createdAccountId is null or e.createdAccountId = :createdAccountId) " +
            "and (:createdDateTimeGE is null or e.createdDateTime >= :createdDateTimeGE) " +
            "and (:createdDateTimeLE is null or e.createdDateTime <= :createdDateTimeLE)")
    Page<TaskFileEntryEntity> query(@Param("orgId") String orgId,
                                    @Param("appId") String appId,
                                    @Param("deviceId") String deviceId,
                                    @Param("typeCode") String typeCode,
                                    @Param("statusCodeIn") List<String> statusCodeIn,
                                    @Param("statusCodeInIgnored") boolean statusCodeInIgnored,
                                    @Param("createdAccountId") String createdAccountId,
                                    @Param("createdDateTimeGE") DateTime createdDateTimeGE,
                                    @Param("createdDateTimeLE") DateTime createdDateTimeLE,
                                    Pageable pageable);

}
