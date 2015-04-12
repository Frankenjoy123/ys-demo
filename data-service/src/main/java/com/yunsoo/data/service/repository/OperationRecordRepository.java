package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OperationRecordEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Zhe on 2015/4/8.
 */
public interface OperationRecordRepository extends CrudRepository<OperationRecordEntity, Long> {

    Iterable<OperationRecordEntity> findByOperationType(Integer operationType);

    Iterable<OperationRecordEntity> findByAccountId(Long accountId);

    Iterable<OperationRecordEntity> findByAccountIdAndOperationType(Long accountId, Integer operationType);
}