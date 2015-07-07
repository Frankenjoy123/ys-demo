package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.AccountPermissionPolicyEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/13
 * Descriptions:
 */
public interface AccountPermissionPolicyRepository extends CrudRepository<AccountPermissionPolicyEntity, String> {

    List<AccountPermissionPolicyEntity> findByAccountId(String accountId);
}
