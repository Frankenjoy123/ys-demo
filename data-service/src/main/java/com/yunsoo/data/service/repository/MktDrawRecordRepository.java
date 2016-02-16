package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MktDrawRecordEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
public interface MktDrawRecordRepository extends FindOneAndSaveRepository<MktDrawRecordEntity, String> {

    List<MktDrawRecordEntity> findByProductKey(String productKey);

}
