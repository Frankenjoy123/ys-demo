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
}
