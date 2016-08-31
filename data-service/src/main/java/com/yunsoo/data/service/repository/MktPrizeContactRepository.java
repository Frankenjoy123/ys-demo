package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MktPrizeContactEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2016/7/25
 * Descriptions:
 */
public interface MktPrizeContactRepository extends FindOneAndSaveRepository<MktPrizeContactEntity, String> {
    List<MktPrizeContactEntity> findByMktPrizeId(String mktPrizeId);

}
