package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MktDrawPrizeEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
public interface MktDrawPrizeRepository extends FindOneAndSaveRepository<MktDrawPrizeEntity, String> {

    List<MktDrawPrizeEntity> findByProductKey(String productKey);

}
