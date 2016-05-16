package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.AccountTokenEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/20
 * Descriptions:
 */
public interface AccountTokenRepository extends FindOneAndSaveRepository<AccountTokenEntity, String> {

    List<AccountTokenEntity> findByPermanentToken(String permanentToken);

    List<AccountTokenEntity> findByDeviceId(String deviceId);

}
