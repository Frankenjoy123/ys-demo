package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductBaseEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2015/7/20
 * Descriptions:
 */
public interface ProductBaseRepository extends FindOneAndSaveRepository<ProductBaseEntity, String> {

    List<ProductBaseEntity> findByOrgId(String orgId);

}