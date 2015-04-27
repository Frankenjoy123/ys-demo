package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.LogisticsCheckPointEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Jerry on 4/21/2015.
 */
public interface LogisticsCheckPointRepository extends PagingAndSortingRepository<LogisticsCheckPointEntity, String> {
    Page<LogisticsCheckPointEntity> findByOrgId(String orgId, Pageable pageable);
}
