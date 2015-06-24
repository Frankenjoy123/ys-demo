package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.AccountPermissionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/12
 * Descriptions:
 */
public interface AccountPermissionRepository extends CrudRepository<AccountPermissionEntity, String> {

    List<AccountPermissionEntity> findByAccountId(String accountId);

}

