package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductKeyBatchEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
public interface ProductKeyBatchRepository extends FindOneAndSaveRepository<ProductKeyBatchEntity, String> {

    Page<ProductKeyBatchEntity> findByOrgIdAndStatusCodeIn(String orgId, List<String> statusCodes, Pageable pageable);

    Page<ProductKeyBatchEntity> findByOrgIdAndProductBaseIdAndStatusCodeIn(String orgId, String productBaseId, List<String> statusCodes, Pageable pageable);

    @Query("select sum(o.quantity) from #{#entityName} o where " +
            "(:orgId is null or o.orgId = :orgId) " +
            "and (:productBaseId is null or o.productBaseId = :productBaseId)")
    Long sumQuantity(@Param("orgId") String orgId,
                     @Param("productBaseId") String productBaseId);

    @Query("select sum(o.quantity) from #{#entityName} o where " +
            "(:orgId is null or o.orgId = :orgId) " +
            "and (:productBaseId is null or o.productBaseId = :productBaseId) " +
            "and (o.statusCode in :statusCodeIn)")
    Long sumQuantity(@Param("orgId") String orgId,
                     @Param("productBaseId") String productBaseId,
                     @Param("statusCodeIn") List<String> statusCodeIn);

    Integer countByRestQuantityLessThanAndStatusCodeIn(Integer quantity,  List<String> statusCodeIn);

    Page<ProductKeyBatchEntity> findByOrgIdAndProductKeyTypeCodesAndStatusCodeIn(String orgId, String typeCode, List<String> statusCodeIn, Pageable pageable);

    Page<ProductKeyBatchEntity> findByOrgIdAndProductKeyTypeCodesNotAndStatusCodeIn(String orgId, String typeCode, List<String> statusCodeIn, Pageable pageable);

}
