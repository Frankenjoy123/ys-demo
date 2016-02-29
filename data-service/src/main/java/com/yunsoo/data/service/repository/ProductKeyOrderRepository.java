package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductKeyOrderEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by  : Zhe
 * Created on  : 2015/4/16
 * Descriptions:
 */
public interface ProductKeyOrderRepository extends FindOneAndSaveRepository<ProductKeyOrderEntity, String> {

    List<ProductKeyOrderEntity> findAll(Iterable<String> ids);

    @Query("select o from #{#entityName} o where " +
            "(:orgId is null or o.orgId = :orgId) " +
            "and (:active is null or o.active = :active) " +
            "and (:remainGE is null or o.remain >= :remainGE) " +
            "and (:expireDateTimeGE is null or o.expireDateTime is null or o.expireDateTime >= :expireDateTimeGE)" +
            "and (:productBaseId is null or o.productBaseId is null or o.productBaseId = :productBaseId)")
    Page<ProductKeyOrderEntity> query(@Param("orgId") String orgId,
                                      @Param("active") Boolean active,
                                      @Param("remainGE") Long remainGE,
                                      @Param("expireDateTimeGE") DateTime expireDateTimeGE,
                                      @Param("productBaseId") String productBaseId,
                                      Pageable pageable);

    List<ProductKeyOrderEntity> save(Iterable<ProductKeyOrderEntity> entities);
}
