package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.BrandEntity;
import com.yunsoo.data.service.entity.OrganizationEntity;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by yan on 3/9/2016.
 */
public interface BrandRepository extends CrudRepository<BrandEntity, String> {

    @Query(" select be from BrandEntity be inner join be.organization o where be.carrierId= :carrierId and " +
            "( :status is null or o.statusCode = :status) and ( :name is null or o.name= :name) " +
            "and (:endTime is null or o.createdDateTime <= :endTime) and  (:startTime is null or o.createdDateTime >= :startTime) " +
            "and (:searchText is null or o.name like ('%' || :searchText || '%') or be.contactName like ('%' || :searchText || '%') or be.contactMobile like ('%' || :searchText || '%') or be.email like ('%' || :searchText || '%'))" +
            "order by o.createdDateTime Desc")
    Page<BrandEntity> filter(@Param("carrierId")String carrierId, @Param("status")String status,
                             @Param("name")String name, @Param("searchText")String searchText,
                             @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime,
                             Pageable pageable);

    @Query("select be.orgId from BrandEntity be where carrierId = ?1")
    List<String> findOrgIdByCarrierId(String carrierId);

    @Query(" select count(o.id) from BrandEntity be inner join be.organization o where be.carrierId= :carrierId and " +
            "( :status is null or o.statusCode = :status)")
    int countByCarrierIdAndStatus(@Param("carrierId")String carrierId, @Param("status")String status);
}
