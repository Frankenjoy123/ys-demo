package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OperationLogEntity;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by yan on 7/6/2016.
 */
public interface OperationLogRepository extends CrudRepository<OperationLogEntity, String> {

    @Query("select e from OperationLogEntity e where e.createdAccountId in :accountIds and (:appId is null or e.createdAppId = :appId) and " +
            "(:start is null or e.createdDateTime>= :start)")
    Page<OperationLogEntity> query(@Param("accountIds")List<String> accountIds,
                                   @Param("appId")String appId, @Param("start")DateTime start, Pageable pageable);
}
