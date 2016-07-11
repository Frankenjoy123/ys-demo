package com.yunsoo.auth.service;

import com.yunsoo.auth.dao.entity.AccountGroupEntity;
import com.yunsoo.auth.dao.repository.AccountGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-07-07
 * Descriptions:
 */
@Service
public class AccountGroupService {

    @Autowired
    private AccountGroupRepository accountGroupRepository;


    public List<String> getGroupIdsByAccountId(String accountId) {
        return accountGroupRepository.findByAccountId(accountId)
                .stream()
                .map(AccountGroupEntity::getGroupId)
                .collect(Collectors.toList());
    }

    public List<String> getAccountIdsByGroupId(String groupId) {
        return accountGroupRepository.findByGroupId(groupId)
                .stream()
                .map(AccountGroupEntity::getAccountId)
                .collect(Collectors.toList());
    }


}
