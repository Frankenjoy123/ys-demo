package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserScanRecordEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by:   Lijian
 * Created on:   2015/8/24
 * Descriptions:
 */
public interface UserScanRecordRepository extends FindOneAndSaveRepository<UserScanRecordEntity, String> {

    Page<UserScanRecordEntity> findByProductKey(String productKey, Pageable pageable);

}
