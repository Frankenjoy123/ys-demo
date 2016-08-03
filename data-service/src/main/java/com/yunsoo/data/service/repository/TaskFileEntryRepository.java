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
            "and (:productBaseId is null or e.productBaseId = :productBaseId) " +
            "and (:createdAccountId is null or e.createdAccountId = :createdAccountId) " +
            "and (:createdDateTimeGE is null or e.createdDateTime >= :createdDateTimeGE) " +
            "and (:createdDateTimeLE is null or e.createdDateTime <= :createdDateTimeLE)")
    Page<TaskFileEntryEntity> query(@Param("orgId") String orgId,
                                    @Param("appId") String appId,
                                    @Param("deviceId") String deviceId,
                                    @Param("typeCode") String typeCode,
                                    @Param("statusCodeIn") List<String> statusCodeIn,
                                    @Param("statusCodeInIgnored") boolean statusCodeInIgnored,
                                    @Param("productBaseId") String productBaseId,
                                    @Param("createdAccountId") String createdAccountId,
                                    @Param("createdDateTimeGE") DateTime createdDateTimeGE,
                                    @Param("createdDateTimeLE") DateTime createdDateTimeLE,
                                    Pageable pageable);

    @Query("select sum(e.packageCount) as package, sum(e.productCount) as product from TaskFileEntryEntity e " +
            "where e.orgId = :orgId " +
            "and (:appId is null or e.appId = :appId) " +
            "and (:deviceId is null or e.deviceId = :deviceId) " +
            "and (:typeCode is null or e.typeCode = :typeCode) " +
            "and (e.statusCode in (:statusCodeIn) or :statusCodeInIgnored = true) " +
            "and (:createdDateTimeGE is null or e.createdDateTime >= :createdDateTimeGE) " +
            "and (:createdDateTimeLE is null or e.createdDateTime <= :createdDateTimeLE)")
    Object getTotal(@Param("orgId") String orgId,
                 @Param("appId") String appId,
                 @Param("deviceId") String deviceId,
                 @Param("typeCode") String typeCode,
                 @Param("createdDateTimeGE") DateTime start,
                 @Param("createdDateTimeLE") DateTime end,  @Param("statusCodeIn") List<String> statusCodeIn,
                    @Param("statusCodeInIgnored") boolean statusCodeInIgnored);


    @Query("select e.deviceId, sum(e.packageCount) as package, sum(e.productCount) as product from TaskFileEntryEntity e " +
            "where e.deviceId in :deviceId " +
            "and (:typeCode is null or e.typeCode = :typeCode) " +
            "and (e.statusCode in (:statusCodeIn) or :statusCodeInIgnored = true) " +
            "and (:createdDateTimeGE is null or e.createdDateTime >= :createdDateTimeGE) " +
            "and (:createdDateTimeLE is null or e.createdDateTime <= :createdDateTimeLE) group by e.deviceId")
    List<Object> getTotalByDevice(@Param("deviceId") List<String> deviceId,
                    @Param("typeCode") String typeCode,
                    @Param("createdDateTimeGE") DateTime start,
                    @Param("createdDateTimeLE") DateTime end,  @Param("statusCodeIn") List<String> statusCodeIn,
                                  @Param("statusCodeInIgnored") boolean statusCodeInIgnored);



    @Query(value = "select substring(CONVERT_TZ(e.created_datetime,'+00:00','+08:00'),1, 10) as day, sum(e.package_count) as package, sum(e.product_count) as product from task_file_entry e " +
            "where  e.device_id = :deviceId " +
            "and (:typeCode is null or e.type_code = :typeCode) " +
            "and (e.status_code in (:statusCodeIn) or :statusCodeInIgnored = true) " +
            "and (:createdDateTimeGE is null or e.created_datetime >= :createdDateTimeGE) " +
            "and (:createdDateTimeLE is null or e.created_datetime <= :createdDateTimeLE) " +
            "group by day", nativeQuery = true)
    List<Object> getTotalByDate(  @Param("deviceId") String deviceId,
                                  @Param("typeCode") String typeCode,
                                  @Param("createdDateTimeGE") DateTime start,
                                  @Param("createdDateTimeLE") DateTime end,  @Param("statusCodeIn") List<String> statusCodeIn,
                                  @Param("statusCodeInIgnored") boolean statusCodeInIgnored);
}
