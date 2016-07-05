package com.yunsoo.auth.dao.repository;

import com.yunsoo.auth.dao.entity.AccountLoginLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * Created by:   Lijian
 * Created on:   2016-05-03
 * Descriptions:
 */
public interface AccountLoginLogRepository extends Repository<AccountLoginLogEntity, String> {

    Page<AccountLoginLogEntity> findByAccountId(String accountId, Pageable pageable);

    AccountLoginLogEntity save(AccountLoginLogEntity entity);

}
