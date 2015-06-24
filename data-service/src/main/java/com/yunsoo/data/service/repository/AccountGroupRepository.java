package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.AccountGroupEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/23
 * Descriptions:
 */
public interface AccountGroupRepository extends Repository<AccountGroupEntity, String> {

    List<AccountGroupEntity> findByAccountId(String accountId);

    List<AccountGroupEntity> findByGroupId(String groupId);
}
