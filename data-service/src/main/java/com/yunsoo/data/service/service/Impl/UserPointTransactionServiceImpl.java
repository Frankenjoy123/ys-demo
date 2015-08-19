package com.yunsoo.data.service.service.Impl;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.data.service.entity.UserPointEntity;
import com.yunsoo.data.service.entity.UserPointTransactionEntity;
import com.yunsoo.data.service.repository.UserPointRepository;
import com.yunsoo.data.service.repository.UserPointTransactionRepository;
import com.yunsoo.data.service.service.UserPointTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by:   Lijian
 * Created on:   2015/8/19
 * Descriptions:
 */
@Service("userPointTransactionService")
public class UserPointTransactionServiceImpl implements UserPointTransactionService {

    @Autowired
    private UserPointRepository userPointRepository;

    @Autowired
    private UserPointTransactionRepository userPointTransactionRepository;


    @Transactional
    @Override
    public UserPointTransactionEntity commit(UserPointTransactionEntity entity) {
        if (!LookupCodes.UserPointTransactionStatus.COMMITTED.equals(entity.getStatusCode())) {
            UserPointEntity upEntity = userPointRepository.findOne(entity.getUserId());
            if (upEntity != null) {
                upEntity.setPoint(upEntity.getPoint() + entity.getPoint());
                entity.setStatusCode(LookupCodes.UserPointTransactionStatus.COMMITTED);
                userPointRepository.save(upEntity);
                return userPointTransactionRepository.save(entity);
            }
        }
        return entity;
    }

    @Transactional
    @Override
    public UserPointTransactionEntity rollback(UserPointTransactionEntity entity) {
        if (LookupCodes.UserPointTransactionStatus.COMMITTED.equals(entity.getStatusCode())) {
            UserPointEntity upEntity = userPointRepository.findOne(entity.getUserId());
            if (upEntity != null) {
                upEntity.setPoint(upEntity.getPoint() - entity.getPoint());
                entity.setStatusCode(LookupCodes.UserPointTransactionStatus.ROLLBACK);
                userPointRepository.save(upEntity);
                return userPointTransactionRepository.save(entity);
            }
        }
        return entity;
    }
}
