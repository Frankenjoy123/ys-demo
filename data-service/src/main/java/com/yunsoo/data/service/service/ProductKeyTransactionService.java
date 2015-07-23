package com.yunsoo.data.service.service;

import com.yunsoo.data.service.entity.ProductKeyTransactionDetailEntity;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/22
 * Descriptions:
 */
public interface ProductKeyTransactionService {

    List<ProductKeyTransactionDetailEntity> commit(List<ProductKeyTransactionDetailEntity> transactionDetailEntities);

    List<ProductKeyTransactionDetailEntity> rollback(List<ProductKeyTransactionDetailEntity> transactionDetailEntities);

}
