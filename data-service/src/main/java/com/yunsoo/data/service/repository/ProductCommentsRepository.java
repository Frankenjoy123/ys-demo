package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductCommentsEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by:   Haitao
 * Created on:   2015/8/20
 * Descriptions:
 */
public interface ProductCommentsRepository extends FindOneAndSaveRepository<ProductCommentsEntity, String> {

    @Query("select o from #{#entityName} o where " +
            "(o.productBaseId = :productBaseId) " +
            "and (:scoreGE is null or o.score >= :scoreGE) " +
            "and (:scoreLE is null or o.score <= :scoreLE) " +
            "and (:lastCommentDatetimeGE is null or o.createdDateTime >= :lastCommentDatetimeGE)")
    Page<ProductCommentsEntity> query(@Param("productBaseId") String productBaseId,
                                      @Param("scoreGE") Integer scoreGE,
                                      @Param("scoreLE") Integer scoreLE,
                                      @Param("lastCommentDatetimeGE") String lastCommentDatetimeGE,
                                      Pageable pageable);

    void delete(String id);

}