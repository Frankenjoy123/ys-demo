package com.yunsoo.data.service.service.Impl;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.data.service.entity.UserEntity;
import com.yunsoo.data.service.entity.UserPointTransactionEntity;
import com.yunsoo.data.service.repository.UserPointTransactionRepository;
import com.yunsoo.data.service.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private UserPointTransactionRepository userPointTransactionRepository;


    @Transactional
    @Override
    public UserPointTransactionEntity commit(UserPointTransactionEntity entity) {
        if (!LookupCodes.UserPointTransactionStatus.COMMITTED.equals(entity.getStatusCode())) {
            UserEntity userEntity = userRepository.findOne(entity.getUserId());
            if (userEntity != null) {
                userEntity.setPoint(userEntity.getPoint() + entity.getPoint());
                entity.setStatusCode(LookupCodes.UserPointTransactionStatus.COMMITTED);
                userRepository.save(userEntity);
                return userPointTransactionRepository.save(entity);
            }
        }
        return entity;
    }

    @Transactional
    @Override
    public UserPointTransactionEntity rollback(UserPointTransactionEntity entity) {
        if (LookupCodes.UserPointTransactionStatus.COMMITTED.equals(entity.getStatusCode())) {
            UserEntity userEntity = userRepository.findOne(entity.getUserId());
            if (userEntity != null) {
                userEntity.setPoint(userEntity.getPoint() - entity.getPoint());
                entity.setStatusCode(LookupCodes.UserPointTransactionStatus.ROLLBACK);
                userRepository.save(userEntity);
                return userPointTransactionRepository.save(entity);
            }
        }
        return entity;
    }
}
