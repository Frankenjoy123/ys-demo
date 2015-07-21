package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductBaseVersionsEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by haitao on 2015/7/20.
 */
public interface ProductBaseVersionsRepository extends FindOneAndSaveRepository<ProductBaseVersionsEntity, String> {

    List<ProductBaseVersionsEntity> findByProductBaseId(String productBaseId);

    List<ProductBaseVersionsEntity> findByProductBaseIdAndVersion(String productBaseId, String version);

    @Transactional
    void deleteByProductBaseIdAndVersion(String productBaseId, String version);

}
