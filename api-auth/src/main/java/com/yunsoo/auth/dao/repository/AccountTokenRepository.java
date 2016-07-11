package com.yunsoo.auth.dao.repository;

import com.yunsoo.auth.dao.entity.AccountTokenEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/20
 * Descriptions:
 */
public interface AccountTokenRepository extends Repository<AccountTokenEntity, String> {

    List<AccountTokenEntity> findByPermanentToken(String permanentToken);

    List<AccountTokenEntity> findByDeviceId(String deviceId);

    AccountTokenEntity save(AccountTokenEntity entity);

}
