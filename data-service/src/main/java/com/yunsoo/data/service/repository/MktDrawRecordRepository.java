package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MktDrawRecordEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
public interface MktDrawRecordRepository extends FindOneAndSaveRepository<MktDrawRecordEntity, String> {

    List<MktDrawRecordEntity> findByProductKey(String productKey);

    List<MktDrawRecordEntity> findByYsid(String ysId);

    Long countByMarketingId(String marketingId);

    @Query("select count(o.id) from #{#entityName} o where " +
            "(o.marketingId = :marketingId) " +
            "and (:startTime is null or o.createdDateTime >= :startTime) " +
            "and (:endTime is null or o.createdDateTime <= :endTime) ")
    Long sumByMarketingId(@Param("marketingId") String orgId,
                          @Param("startTime") DateTime startTime,
                          @Param("endTime") DateTime endTime);


}
