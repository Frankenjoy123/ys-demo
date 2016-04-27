package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.BrandApplicationHistoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yan on 4/26/2016.
 */
public interface BrandApplicationHistoryRepository extends CrudRepository<BrandApplicationHistoryEntity, String> {

    List<BrandApplicationHistoryEntity> findByBrandId(String id);
}
