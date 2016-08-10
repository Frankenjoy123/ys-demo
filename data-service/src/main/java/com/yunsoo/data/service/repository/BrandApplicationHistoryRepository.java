package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.BrandApplicationHistoryEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by:   yan
 * Created on:   4/26/2016
 * Descriptions:
 */
public interface BrandApplicationHistoryRepository extends FindOneAndSaveRepository<BrandApplicationHistoryEntity, String> {

    List<BrandApplicationHistoryEntity> findByBrandIdOrderByCreatedDateTimeDesc(String id);

}
