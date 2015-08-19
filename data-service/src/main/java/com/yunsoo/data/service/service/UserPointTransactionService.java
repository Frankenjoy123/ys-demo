package com.yunsoo.data.service.service;

import com.yunsoo.data.service.entity.UserPointTransactionEntity;

/**
 * Created by:   Lijian
 * Created on:   2015/8/19
 * Descriptions:
 */
public interface UserPointTransactionService {

    UserPointTransactionEntity commit(UserPointTransactionEntity entity);

    UserPointTransactionEntity rollback(UserPointTransactionEntity entity);
}
