package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.AccountEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/17
 * Descriptions:
 */
public interface AccountRepository extends FindOneAndSaveRepository<AccountEntity, String> {

    @Query("select acc from AccountEntity acc where (:orgId is null or acc.orgId = :orgId) and (:status is null or acc.statusCode = :status) " +
            "and (:searchText is null or acc.identifier like :searchText or concat(acc.lastName, acc.firstName) like :searchText or acc.phone like :searchText or acc.email like :searchText)" +
            "and (:endTime is null or acc.createdDateTime <= :endTime) and  (:startTime is null or acc.createdDateTime >= :startTime) " )
    Page<AccountEntity> query(@Param("orgId")String orgId,@Param("status")String status, @Param("searchText")String searchText,
                              @Param("startTime")DateTime start, @Param("endTime")DateTime end, Pageable pageable);

    List<AccountEntity> findByOrgIdAndIdentifier(String orgId, String identifier);


    Long count();

    Long countByOrgId(String orgId);

    Long countByStatusCodeIn(List<String> statusCodeIn);

    Long countByOrgIdAndStatusCodeIn(String orgId, List<String> statusCodeIn);
}
