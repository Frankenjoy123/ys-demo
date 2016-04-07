package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.PaymentEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

/**
 * Created by  : haitao
 * Created on  : 2016/4/5
 * Descriptions:
 */
public interface PaymentRepository extends FindOneAndSaveRepository<PaymentEntity, String> {
}
