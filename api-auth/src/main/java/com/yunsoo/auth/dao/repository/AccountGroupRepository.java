package com.yunsoo.auth.dao.repository;

import com.yunsoo.auth.dao.entity.AccountGroupEntity;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/23
 * Descriptions:
 */
public interface AccountGroupRepository extends Repository<AccountGroupEntity, String> {

    List<AccountGroupEntity> findByAccountId(String accountId);

    List<AccountGroupEntity> findByGroupId(String groupId);

    List<AccountGroupEntity> findByAccountIdAndGroupId(String accountId, String groupId);

    List<AccountGroupEntity> findAll();

    AccountGroupEntity save(AccountGroupEntity entity);

    @Transactional
    void deleteByAccountIdAndGroupId(String accountId, String groupId);

    @Transactional
    void deleteByAccountId(String accountId);

    @Transactional
    void deleteByGroupId(String groupId);


}
