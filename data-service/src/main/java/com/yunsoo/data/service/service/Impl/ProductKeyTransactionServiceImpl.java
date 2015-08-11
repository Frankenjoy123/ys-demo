package com.yunsoo.data.service.service.Impl;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.data.service.entity.ProductKeyOrderEntity;
import com.yunsoo.data.service.entity.ProductKeyTransactionDetailEntity;
import com.yunsoo.data.service.repository.ProductKeyOrderRepository;
import com.yunsoo.data.service.repository.ProductKeyTransactionDetailRepository;
import com.yunsoo.data.service.service.ProductKeyTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/22
 * Descriptions:
 */
@Service
public class ProductKeyTransactionServiceImpl implements ProductKeyTransactionService {

    @Autowired
    private ProductKeyTransactionDetailRepository productKeyTransactionDetailRepository;

    @Autowired
    private ProductKeyOrderRepository productKeyOrderRepository;


    @Transactional
    @Override
    public List<ProductKeyTransactionDetailEntity> commit(List<ProductKeyTransactionDetailEntity> transactionDetailEntities) {
        List<String> orderIds = transactionDetailEntities.stream()
                .filter(t -> !LookupCodes.ProductKeyTransactionStatus.COMMITTED.equals(t.getStatusCode()))
                .map(ProductKeyTransactionDetailEntity::getOrderId)
                .collect(Collectors.toList());
        List<ProductKeyOrderEntity> orderEntities = productKeyOrderRepository.findAll(orderIds);
        Map<String, ProductKeyOrderEntity> orderEntityMap = orderEntities.stream()
                .collect(Collectors.toMap(ProductKeyOrderEntity::getId, e -> e));

        transactionDetailEntities.forEach(t -> {
            if (!LookupCodes.ProductKeyTransactionStatus.COMMITTED.equals(t.getStatusCode())) {
                ProductKeyOrderEntity e = orderEntityMap.get(t.getOrderId());
                e.setRemain(e.getRemain() - t.getQuantity());
                t.setStatusCode(LookupCodes.ProductKeyTransactionStatus.COMMITTED);
            }
        });

        productKeyOrderRepository.save(orderEntities);
        return productKeyTransactionDetailRepository.save(transactionDetailEntities);
    }

    @Transactional
    @Override
    public List<ProductKeyTransactionDetailEntity> rollback(List<ProductKeyTransactionDetailEntity> transactionDetailEntities) {
        List<String> orderIds = transactionDetailEntities.stream()
                .filter(t -> !LookupCodes.ProductKeyTransactionStatus.ROLLBACK.equals(t.getStatusCode()))
                .map(ProductKeyTransactionDetailEntity::getOrderId)
                .collect(Collectors.toList());
        List<ProductKeyOrderEntity> orderEntities = productKeyOrderRepository.findAll(orderIds);
        Map<String, ProductKeyOrderEntity> orderEntityMap = orderEntities.stream()
                .collect(Collectors.toMap(ProductKeyOrderEntity::getId, e -> e));

        transactionDetailEntities.forEach(t -> {
            if (!LookupCodes.ProductKeyTransactionStatus.ROLLBACK.equals(t.getStatusCode())) {
                ProductKeyOrderEntity e = orderEntityMap.get(t.getOrderId());
                e.setRemain(e.getRemain() + t.getQuantity());
                t.setStatusCode(LookupCodes.ProductKeyTransactionStatus.ROLLBACK);
            }
        });

        productKeyOrderRepository.save(orderEntities);
        return productKeyTransactionDetailRepository.save(transactionDetailEntities);
    }
}
