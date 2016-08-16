package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.BrandApplicationEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by:   yan
 * Created on:   3/16/2016
 * Descriptions:
 */
public interface BrandApplicationRepository extends FindOneAndSaveRepository<BrandApplicationEntity, String> {

    @Query("select e from BrandApplicationEntity e " +
            "where (:carrierId is null or e.carrierId = :carrierId) " +
            "and (:brandName is null or e.brandName = :brandName) " +
            "and (:statusCode is null or e.statusCode = :statusCode) " +
            "and (:hasPayment is null or (:hasPayment = true and e.paymentId is not null) or (:hasPayment = false and e.paymentId is null))" +
            "and (:createdDateTimeGE is null or e.createdDateTime >= :createdDateTimeGE) " +
            "and (:createdDateTimeLE is null or e.createdDateTime <= :createdDateTimeLE)" +
            "and (:searchText is null or e.brandName like ('%' || :searchText || '%') or e.contactName like ('%' || :searchText || '%') or e.contactMobile like ('%' || :searchText || '%') or e.email like ('%' || :searchText || '%'))")
    Page<BrandApplicationEntity> query(@Param("carrierId") String carrierId,
                                       @Param("brandName") String brandName,
                                       @Param("statusCode") String statusCode,
                                       @Param("hasPayment") Boolean hasPayment,
                                       @Param("createdDateTimeGE") DateTime createdDateTimeGE,
                                       @Param("createdDateTimeLE") DateTime createdDateTimeLE,
                                       @Param("searchText") String searchText,
                                       Pageable pageable);

    int countByCarrierIdAndStatusCodeAndPaymentIdIsNull(String carrierId, String statusCode);

    int countByCarrierIdAndStatusCodeAndPaymentIdIsNotNull(String carrierId, String statusCode);

}
