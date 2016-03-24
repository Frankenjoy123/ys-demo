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

    Page<MktDrawPrizeEntity> findByMarketingId(String marketingId, Pageable pageable);

    List<MktDrawPrizeEntity> findByStatusCodeAndAccountType(String statusCode, String accountType);

    Long countByDrawRuleId(String drawRuleId);

    @Query("select o from #{#entityName} o where " +
            "(o.marketingId = :marketingId) " +
            "and (:accountType is null or o.accountType = :accountType) " +
            "and (:statusCode is null or o.statusCode = :statusCode) " +
            "and (:startTime is null or o.createdDateTime >= :startTime) " +
            "and (:endTime is null or o.createdDateTime <= :endTime) ")
    Page<MktDrawPrizeEntity> query(@Param("marketingId") String marketingId,
                                   @Param("accountType") String accountType,
                                   @Param("statusCode") String statusCode,
                                   @Param("startTime") DateTime startTime,
                                   @Param("endTime") DateTime endTime,
                                   Pageable pageable);

}
