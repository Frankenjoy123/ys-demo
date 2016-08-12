package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OrgBrandEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by:   yan
 * Created on:   3/9/2016
 * Descriptions:
 */
public interface OrgBrandRepository extends FindOneAndSaveRepository<OrgBrandEntity, String> {

    @Query("select e from #{#entityName} e " +
            "where e.carrierId = :carrierId " +
            "and (e.orgId in (:orgIdIn) or :orgIdInIgnored = true)" +
            "and (:orgName is null or e.orgName = :orgName) " +
            "and (:categoryId is null or e.categoryId = :categoryId)" +
            "and (:createdDateTimeGE is null or e.createdDateTime >= :createdDateTimeGE) " +
            "and (:createdDateTimeLE is null or e.createdDateTime <= :createdDateTimeLE)" +
            "and (:searchText is null or e.orgName like ('%' || :searchText || '%') or e.contactName like ('%' || :searchText || '%') or e.contactMobile like ('%' || :searchText || '%') or e.email like ('%' || :searchText || '%')) ")
    Page<OrgBrandEntity> query(@Param("carrierId") String carrierId,
                               @Param("orgIdIn") List<String> orgIdIn,
                               @Param("orgIdInIgnored") boolean orgIdInIgnored,
                               @Param("orgName") String orgName,
                               @Param("categoryId") String categoryId,
                               @Param("createdDateTimeGE") DateTime createdDateTimeGE,
                               @Param("createdDateTimeLE") DateTime createdDateTimeLE,
                               @Param("searchText") String searchText,
                               Pageable pageable);

    @Query("select e.orgId from #{#entityName} e where carrierId = ?1")
    List<String> findOrgIdByCarrierId(String carrierId);

    Long countByCarrierId(String carrierId);

}
