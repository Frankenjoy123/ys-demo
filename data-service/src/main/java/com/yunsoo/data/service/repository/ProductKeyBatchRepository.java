package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductKeyBatchEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
public interface ProductKeyBatchRepository extends FindOneAndSaveRepository<ProductKeyBatchEntity, String> {

    @Query("select o from #{#entityName} o where (:orgId is null or o.orgId = :orgId) and (o.statusCode in ('creating', 'available')) " +
            "and (:productBaseId is null or :productBaseId = '' or o.productBaseId = :productBaseId) " +
            "and (o.productKeyTypeCodes != 'package') " +
            "and (:createAccount is null or :createAccount = '' or o.createdAccountId = :createAccount) and (:createdDateTimeStart is null or o.createdDateTime >= :createdDateTimeStart) " +
            "and (:createdDateTimeEnd is null or o.createdDateTime <= :createdDateTimeEnd)")
    Page<ProductKeyBatchEntity> findByFilter(@Param("orgId") String orgId, @Param("productBaseId") String productBaseId, @Param("createAccount") String createAccount,
                                             @Param("createdDateTimeStart") DateTime createdDateTimeStart,
                                             @Param("createdDateTimeEnd") DateTime createdDateTimeEnd, Pageable pageable);

    Page<ProductKeyBatchEntity> findByOrgIdInAndStatusCodeIn(List<String> orgId, List<String> statusCodes, Pageable pageable);

    Page<ProductKeyBatchEntity> findByOrgIdAndProductBaseIdAndStatusCodeIn(String orgId, String productBaseId, List<String> statusCodes, Pageable pageable);

    @Query("select sum(o.quantity) from #{#entityName} o where " +
            "(:orgId is null or o.orgId = :orgId) " +
            "and (:productBaseId is null or o.productBaseId = :productBaseId)" +
            "and (:marketingId is null or o.marketingId = :marketingId)" +
            "and (:startTime is null or o.createdDateTime >= :startTime) " +
            "and (:endTime is null or o.createdDateTime <= :endTime) ")
    Long sumQuantityMarketing(@Param("orgId") String orgId,
                              @Param("productBaseId") String productBaseId,
                              @Param("marketingId") String marketingId,
                              @Param("startTime") DateTime startTime,
                              @Param("endTime") DateTime endTime);

    @Query("select sum(o.quantity) from #{#entityName} o where " +
            "(:orgId is null or o.orgId = :orgId) " +
            "and (:productBaseId is null or o.productBaseId = :productBaseId) " +
            "and (o.statusCode in :statusCodeIn)")
    Long sumQuantity(@Param("orgId") String orgId,
                     @Param("productBaseId") String productBaseId,
                     @Param("statusCodeIn") List<String> statusCodeIn);

    @Query("select sum(o.quantity) from #{#entityName} o where " +
            "(:orgId is null or o.orgId = :orgId) " +
            "and (:productBaseId is null or o.productBaseId = :productBaseId) " +
            "and o.createdDateTime >=:startTime and o.createdDateTime <= :endTime")
    Long sumQuantityTime(@Param("orgId") String orgId,
                         @Param("productBaseId") String productBaseId,
                         @Param("startTime") DateTime startTime,
                         @Param("endTime") DateTime endTime);

    Page<ProductKeyBatchEntity> findByOrgIdAndProductKeyTypeCodesAndStatusCodeInAndCreatedDeviceId(String orgId, String typeCode, List<String> statusCodeIn, String deviceId, Pageable pageable);

    @Query("from #{#entityName} o where " +
            "(o.orgId = :orgId) " +
            "and productKeyTypeCodes != 'package'" +
            "and statusCode in ('creating', 'available') " +
            "and (:productBaseId is null or o.productBaseId = :productBaseId) " +
            "and createdDateTime >=:startTime and createdDateTime <= :endTime")
    List<ProductKeyBatchEntity> queryDailyKeyUsageReport(@Param("orgId") String orgId,
                                                         @Param("productBaseId") String productBaseId, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);

    List<ProductKeyBatchEntity> save(Iterable<ProductKeyBatchEntity> entities);

    List<ProductKeyBatchEntity> findByMarketingId(String marketingId);

    @Query("select distinct partitionId from ProductKeyBatchEntity where orgId= ?1 order by createdDateTime desc")
    List<String> findDistinctPartitionIdByOrgIdOrderByCreatedDateTimeDesc(String orgId);
}
