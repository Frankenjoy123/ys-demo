package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MktSellerEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

/**
 * Created by  : haitao
 * Created on  : 2016/12/6
 * Descriptions:
 */
public interface MktSellerRepository extends FindOneAndSaveRepository<MktSellerEntity, String> {
    MktSellerEntity findOne(String openid);
}
