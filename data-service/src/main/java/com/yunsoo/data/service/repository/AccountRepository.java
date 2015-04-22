package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.AccountEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/17
 * Descriptions:
 */
public interface AccountRepository extends FindOneAndSaveRepository<AccountEntity, String> {

    List<AccountEntity> findByOrgId(String orgId);

    List<AccountEntity> findByOrgIdAndIdentifier(String orgId, String identifier);
}
