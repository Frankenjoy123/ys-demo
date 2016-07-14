package com.yunsoo.auth.service;

import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.dao.entity.AccountGroupEntity;
import com.yunsoo.auth.dao.repository.AccountGroupRepository;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.Group;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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

    @Autowired
    private AccountService accountService;

    @Autowired
    private GroupService groupService;


    public List<String> getGroupIdsByAccountId(String accountId) {
        if (StringUtils.isEmpty(accountId)) {
            return new ArrayList<>();
        }
        return accountGroupRepository.findByAccountId(accountId)
                .stream()
                .map(AccountGroupEntity::getGroupId)
                .collect(Collectors.toList());
    }

    public List<Group> getGroupsByAccountId(String accountId) {
        List<String> ids = getGroupIdsByAccountId(accountId);
        return groupService.getByIds(ids);
    }

    public List<String> getAccountIdsByGroupId(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return new ArrayList<>();
        }
        return accountGroupRepository.findByGroupId(groupId)
                .stream()
                .map(AccountGroupEntity::getAccountId)
                .collect(Collectors.toList());
    }

    public List<Account> getAccountsByGroupId(String groupId) {
        List<String> ids = getAccountIdsByGroupId(groupId);
        return accountService.getByIds(ids);
    }


    @Transactional
    public void putAccountGroup(String accountId, String groupId) {
        if (StringUtils.isEmpty(accountId) || StringUtils.isEmpty(groupId)) {
            return;
        }
        if (accountGroupRepository.findByAccountIdAndGroupId(accountId, groupId).size() == 0) {
            AccountGroupEntity entity = new AccountGroupEntity();
            entity.setAccountId(accountId);
            entity.setGroupId(groupId);
            entity.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
            entity.setCreatedDateTime(DateTime.now());
            accountGroupRepository.save(entity);
        }
    }

    @Transactional
    public void putAccountGroupsByAccount(Account account, List<String> groupIds) {
        String accountId = account.getId();
        String createdAccountId = AuthUtils.getCurrentAccount().getId();
        DateTime createdDateTime = DateTime.now();
        List<AccountGroupEntity> currentEntities = accountGroupRepository.findByAccountId(accountId);
        List<String> currentGroupIds = currentEntities.stream().map(AccountGroupEntity::getGroupId).collect(Collectors.toList());
        List<String> allGroupIds = groupService.getByOrgId(account.getOrgId()).stream().map(Group::getId).collect(Collectors.toList());

        List<AccountGroupEntity> forDeleteEntities = currentEntities.stream().filter(age -> !groupIds.contains(age.getGroupId())).collect(Collectors.toList());

        List<AccountGroupEntity> forSaveEntities = groupIds.stream()
                .filter(gId -> allGroupIds.contains(gId) && !currentGroupIds.contains(gId))
                .map(gId -> {
                    AccountGroupEntity age = new AccountGroupEntity();
                    age.setAccountId(accountId);
                    age.setGroupId(gId);
                    age.setCreatedAccountId(createdAccountId);
                    age.setCreatedDateTime(createdDateTime);
                    return age;
                })
                .collect(Collectors.toList());

        accountGroupRepository.delete(forDeleteEntities);
        accountGroupRepository.save(forSaveEntities);
    }

    @Transactional
    public void putAccountGroupsByGroup(Group group, List<String> accountIds) {
        String groupId = group.getId();
        String createdAccountId = AuthUtils.getCurrentAccount().getId();
        DateTime createdDateTime = DateTime.now();
        List<AccountGroupEntity> currentEntities = accountGroupRepository.findByGroupId(groupId);
        List<String> currentAccountIds = currentEntities.stream().map(AccountGroupEntity::getAccountId).collect(Collectors.toList());
        List<String> allAccountIds = accountService.getByOrgId(group.getOrgId()).stream().map(Account::getId).collect(Collectors.toList());

        List<AccountGroupEntity> forDeleteEntities = currentEntities.stream().filter(age -> !accountIds.contains(age.getAccountId())).collect(Collectors.toList());

        List<AccountGroupEntity> forSaveEntities = accountIds.stream()
                .filter(aId -> allAccountIds.contains(aId) && !currentAccountIds.contains(aId))
                .map(aId -> {
                    AccountGroupEntity age = new AccountGroupEntity();
                    age.setAccountId(aId);
                    age.setGroupId(groupId);
                    age.setCreatedAccountId(createdAccountId);
                    age.setCreatedDateTime(createdDateTime);
                    return age;
                })
                .collect(Collectors.toList());

        accountGroupRepository.delete(forDeleteEntities);
        accountGroupRepository.save(forSaveEntities);
    }

    public void deleteAccountGroupByAccountIdAndGroupId(String accountId, String groupId) {
        accountGroupRepository.deleteByAccountIdAndGroupId(accountId, groupId);
    }

    public void deleteAccountGroupsByGroupId(String groupId) {
        accountGroupRepository.deleteByGroupId(groupId);
    }

}
