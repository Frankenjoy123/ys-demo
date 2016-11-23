package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MktDrawPrizeEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
public interface MktDrawPrizeRepository extends FindOneAndSaveRepository<MktDrawPrizeEntity, String> {

    MktDrawPrizeEntity findOne(String drawRecordId);

    List<MktDrawPrizeEntity> findByProductKey(String productKey);

    List<MktDrawPrizeEntity> findByScanRecordId(String scanRecordId);

    Page<MktDrawPrizeEntity> findByMarketingId(String marketingId, Pageable pageable);

    List<MktDrawPrizeEntity> findByStatusCodeAndAccountType(String statusCode, String accountType);

    List<MktDrawPrizeEntity> findTop10ByMarketingIdAndStatusCodeInOrderByCreatedDateTimeDesc(String marketingId, List<String> statusCode);

    List<MktDrawPrizeEntity> findTop10ByMarketingIdAndStatusCodeInAndDrawRecordIdInOrderByCreatedDateTimeDesc(String marketingId, List<String> statusCode, List<String> recordIds);

    Long countByMarketingId(String marketingId);

    Long countByMarketingIdIn(List<String> marketingIdIn);

    Long countByDrawRuleId(String drawRuleId);

    @Query("select count(o.drawRecordId) from #{#entityName} o where " +
            "(o.drawRuleId = :drawRuleId) " +
            "and (:startTime is null or o.createdDateTime >= :startTime) " +
            "and (:endTime is null or o.createdDateTime <= :endTime) ")
    Long sumDrawRuleId(@Param("drawRuleId") String drawRuleId, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);


    @Query("select o from #{#entityName} o where " +
            "(o.marketingId = :marketingId) " +
            "and (:accountType is null or o.accountType = :accountType) " +
            "and (:prizeTypeCode is null or o.prizeTypeCode = :prizeTypeCode) " +
            "and (:statusCode is null or o.statusCode = :statusCode) " +
            "and (:startTime is null or o.createdDateTime >= :startTime) " +
            "and (:endTime is null or o.createdDateTime <= :endTime) ")
    Page<MktDrawPrizeEntity> query(@Param("marketingId") String marketingId,
                                   @Param("accountType") String accountType,
                                   @Param("prizeTypeCode") String prizeTypeCode,
                                   @Param("statusCode") String statusCode,
                                   @Param("startTime") DateTime startTime,
                                   @Param("endTime") DateTime endTime,
                                   Pageable pageable);

}
