package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductBaseEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by haitao on 2015/7/20.
 */
public interface ProductBaseRepository extends FindOneAndSaveRepository<ProductBaseEntity, String> {

    List<ProductBaseEntity> findByVersion(String version);

    void delete(String id);
}
