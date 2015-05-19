package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.LogisticsCheckActionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Jerry on 4/27/2015.
 */
public interface LogisticsCheckActionRepository extends PagingAndSortingRepository<LogisticsCheckActionEntity, Integer> {
    Page<LogisticsCheckActionEntity> findByOrgId(String orgId, Pageable pageable);
}
