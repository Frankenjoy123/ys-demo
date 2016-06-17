package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.EMREventEntity;
import com.yunsoo.data.service.entity.EMRUserEntity;
import com.yunsoo.data.service.entity.MarketUserLocationAnalysisEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EMREventRepository extends FindOneAndSaveRepository<EMREventEntity, String>, CustomEMREventRepository {

    @Query("select ev.province, count(1) from #{#entityName} ev where ev.marketingId = :markertingId and isPriced =1  group by ev.province")
    List<Object[]> queryRewardLocationReport(@Param("markertingId") String marketingId);

    @Query("select o from #{#entityName} o where " +
            "(:orgId is null or :orgId = '' or o.orgId = :orgId) " +
            "and (:userId is null or :userId = '' or o.userId = :userId) " +
            "and (:ysId is null or :ysId = '' or o.ysId = :ysId) " +
            "and (:eventDateTimeStart is null or o.eventDateTime >= :eventDateTimeStart) " +
            "and (:eventDateTimeEnd is null or o.eventDateTime <= :eventDateTimeEnd)")
    Page<EMREventEntity> findByFilter(@Param("orgId") String orgId, @Param("userId") String userId, @Param("ysId") String ysId,
                                      @Param("eventDateTimeStart") DateTime eventDateTimeStart,
                                      @Param("eventDateTimeEnd") DateTime eventDateTimeEnd, Pageable pageable);
}
