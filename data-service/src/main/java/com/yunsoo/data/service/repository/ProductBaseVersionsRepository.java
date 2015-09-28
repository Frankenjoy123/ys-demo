package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductBaseVersionsEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2015/7/20
 * Descriptions:
 */
public interface ProductBaseVersionsRepository extends FindOneAndSaveRepository<ProductBaseVersionsEntity, String> {

    List<ProductBaseVersionsEntity> findByProductBaseIdOrderByVersionAsc(String productBaseId);

    List<ProductBaseVersionsEntity> findByProductBaseIdAndVersion(String productBaseId, Integer version);

    List<ProductBaseVersionsEntity> findByProductBaseIdIn(List<String> productBaseIds);

    @Transactional
    void deleteByProductBaseIdAndVersion(String productBaseId, Integer version);

}
