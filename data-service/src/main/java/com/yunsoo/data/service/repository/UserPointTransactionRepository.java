package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserPointTransactionEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by:   Lijian
 * Created on:   2015/8/17
 * Descriptions:
 */
public interface UserPointTransactionRepository extends FindOneAndSaveRepository<UserPointTransactionEntity, String> {

    Page<UserPointTransactionEntity> findByUserId(String userId, Pageable pageable);
}
