package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.LocationEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2015/9/6
 * Descriptions:
 */
public interface LocationRepository extends FindOneAndSaveRepository<LocationEntity, String> {

    List<LocationEntity> findByParentId(String parentId);

    List<LocationEntity> findAll();

}
