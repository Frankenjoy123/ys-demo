package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductBaseVersionsEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by haitao on 2015/7/20.
 */
public interface ProductBaseVersionsRepository extends FindOneAndSaveRepository<ProductBaseVersionsEntity, String> {

    List<ProductBaseVersionsEntity> findByProductBaseId(String productBaseId);

    void delete(String id);

}
