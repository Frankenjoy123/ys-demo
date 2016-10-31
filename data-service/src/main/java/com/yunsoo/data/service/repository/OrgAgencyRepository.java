package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OrgAgencyEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2015/9/2
 * Descriptions:
 */
public interface OrgAgencyRepository extends FindOneAndSaveRepository<OrgAgencyEntity, String> {

    Page<OrgAgencyEntity> findByOrgIdAndStatusCodeIn(String orgId, List<String> statusCodes, Pageable pageable);

    @Query("select age from OrgAgencyEntity age where (:orgId is null or age.orgId = :orgId) " +
            "and (:parentId is null or age.parentId = :parentId) " +
            "and (:searchText is null or age.name like ('%' || :searchText || '%') or age.address like ('%' || :searchText || '%')  " +
            "or age.agencyResponsible like ('%' || :searchText || '%')  or age.agencyPhone like ('%' || :searchText || '%') ) " +
            "and (:endTime is null or age.createdDateTime <= :endTime) and  (:startTime is null or age.createdDateTime >= :startTime) and age.statusCode <> 'deleted'")
    Page<OrgAgencyEntity> query(@Param("orgId") String orgId, @Param("searchText") String searchText,@Param("parentId") String parentId,
                                @Param("startTime") DateTime start, @Param("endTime") DateTime end, Pageable pageable);




    List<OrgAgencyEntity> findByOrgIdAndIdInAndStatusCode(String orgId, List<String> idList, String statusCode);

    int countByParentIdAndStatusCode(String parentId, String status);

}
