package com.yunsoo.auth.service;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.api.util.PageUtils;
import com.yunsoo.auth.config.AuthCacheConfig;
import com.yunsoo.auth.dao.entity.AccountEntity;
import com.yunsoo.auth.dao.repository.AccountRepository;
import com.yunsoo.auth.dao.repository.OrganizationRepository;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.AccountCreationRequest;
import com.yunsoo.common.util.HashUtils;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-07-06
 * Descriptions:
 */
@AuthCacheConfig
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrganizationRepository organizationRepository;


    @Cacheable(key = "'account:' + #id")
    public Account getById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        AccountEntity entity = accountRepository.findOne(id);
        return toAccount(entity);
    }

    public Account getByOrgIdAndIdentifier(String orgId, String identifier) {
        if (StringUtils.isEmpty(orgId) || StringUtils.isEmpty(identifier)) {
            return null;
        }
        List<AccountEntity> entities = accountRepository.findByOrgIdAndIdentifier(orgId, identifier);
        if (entities.size() == 0) {
            return null;
        }
        return toAccount(entities.get(0));
    }

    public List<Account> getByIds(List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return accountRepository.findAll(ids).stream().map(this::toAccount).collect(Collectors.toList());
    }

    public List<Account> getByOrgId(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            return new ArrayList<>();
        }
        return accountRepository.findByOrgId(orgId).stream().map(this::toAccount).collect(Collectors.toList());
    }

    public List<Account> getByTypeCode(String typeCode, String orgId){
        if (StringUtils.isEmpty(typeCode) || StringUtils.isEmpty(orgId)) {
            return new ArrayList<>();
        }
        return accountRepository.findByTypeCodeAndOrgId(typeCode, orgId).stream().map(this::toAccount).collect(Collectors.toList());
    }

    public Page<Account> search(String orgId, String statusCode, String searchText, DateTime createdDateTimeGE, DateTime createdDateTimeLE, Pageable pageable) {
        if (StringUtils.isEmpty(orgId)) {
            return Page.empty();
        }

        return PageUtils.convert(accountRepository.search(orgId, statusCode, searchText, createdDateTimeGE, createdDateTimeLE, Constants.AccountType.ENTERPRISE, pageable)).map(this::toAccount);
    }


    public long count(String orgId, List<String> statusCodeIn) {
        return statusCodeIn == null
                ? accountRepository.countByOrgId(orgId)
                : accountRepository.countByOrgIdAndStatusCodeIn(orgId, statusCodeIn);
    }

    /**
     * assume the password is hashed if request.hashSalt is not empty.
     * otherwise new hashSalt is generated and the password is hashed before saved.
     *
     * @param request not null
     * @return new created account
     */
    @Transactional
    public Account create(AccountCreationRequest request) {
        if (organizationRepository.findOne(request.getOrgId()) == null) {
            throw new UnprocessableEntityException("organization not exists with id: " + request.getOrgId());
        }
        if (getByOrgIdAndIdentifier(request.getOrgId(), request.getIdentifier()) != null) {
            throw new ConflictException("account already exists with the same identifier");
        }
        AccountEntity entity = new AccountEntity();
        entity.setOrgId(request.getOrgId());
        entity.setIdentifier(request.getIdentifier());
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setStatusCode(Constants.AccountStatus.AVAILABLE);
        String hashSalt = request.getHashSalt();
        if (StringUtils.isEmpty(hashSalt)) {
            hashSalt = RandomUtils.generateString(8);
            entity.setPassword(hashPassword(request.getPassword(), hashSalt));
        } else {
            entity.setPassword(request.getPassword()); //keep the password as is if there's hash salt
        }
        entity.setHashSalt(hashSalt);
        if (Constants.SYSTEM_ACCOUNT_ID.equals(request.getCreatedAccountId())) {
            entity.setCreatedAccountId(request.getCreatedAccountId());
        } else {
            entity.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        }
        entity.setCreatedDateTime(DateTime.now());
        return toAccount(accountRepository.save(entity));
    }

    @CacheEvict(key = "'account:' + #account.id")
    @Transactional
    public void patchUpdate(Account account) {
        if (StringUtils.isEmpty(account.getId())) {
            return;
        }
        AccountEntity entity = accountRepository.findOne(account.getId());
        if (entity != null) {
            if (account.getFirstName() != null) entity.setFirstName(account.getFirstName());
            if (account.getLastName() != null) entity.setLastName(account.getLastName());
            if (account.getEmail() != null) entity.setEmail(account.getEmail());
            if (account.getPhone() != null) entity.setPhone(account.getPhone());
            entity.setModifiedAccountId(AuthUtils.getCurrentAccount().getId());
            entity.setModifiedDateTime(DateTime.now());
            accountRepository.save(entity);
        }
    }

    @CacheEvict(key = "'account:' + #accountId")
    @Transactional
    public void updateStatus(String accountId, String statusCode) {
        if (StringUtils.isEmpty(accountId) || !Constants.AccountStatus.ALL.contains(statusCode)) {
            return;
        }
        AccountEntity entity = accountRepository.findOne(accountId);
        if (entity != null) {
            entity.setStatusCode(statusCode);
            entity.setModifiedAccountId(AuthUtils.getCurrentAccount().getId());
            entity.setModifiedDateTime(DateTime.now());
            accountRepository.save(entity);
        }
    }

    @CacheEvict(key = "'account:' + #accountId")
    @Transactional
    public void updatePassword(String accountId, String rawNewPassword) {
        if (StringUtils.isEmpty(accountId) || StringUtils.isEmpty(rawNewPassword)) {
            return;
        }
        AccountEntity entity = accountRepository.findOne(accountId);
        if (entity != null) {
            String hashSalt = RandomUtils.generateString(8);
            entity.setPassword(hashPassword(rawNewPassword, hashSalt));
            entity.setHashSalt(hashSalt);
            entity.setModifiedAccountId(AuthUtils.getCurrentAccount().getId());
            entity.setModifiedDateTime(DateTime.now());
            accountRepository.save(entity);
        }
    }

    public boolean validateAccount(Account account) {
        return account != null && Constants.AccountStatus.AVAILABLE.equals(account.getStatusCode());
    }

    public boolean validatePassword(String rawPassword, String hashSalt, String password) {
        return rawPassword != null && rawPassword.length() > 0
                && hashSalt != null && hashPassword(rawPassword, hashSalt).equals(password);
    }


    private String hashPassword(String rawPassword, String hashSalt) {
        return HashUtils.sha1HexString(rawPassword + hashSalt);
    }

    private Account toAccount(AccountEntity entity) {
        if (entity == null) {
            return null;
        }
        Account account = new Account();
        account.setId(entity.getId());
        account.setOrgId(entity.getOrgId());
        account.setIdentifier(entity.getIdentifier());
        account.setStatusCode(entity.getStatusCode());
        account.setFirstName(entity.getFirstName());
        account.setLastName(entity.getLastName());
        account.setEmail(entity.getEmail());
        account.setPhone(entity.getPhone());
        account.setPassword(entity.getPassword());
        account.setHashSalt(entity.getHashSalt());
        account.setCreatedAccountId(entity.getCreatedAccountId());
        account.setCreatedDateTime(entity.getCreatedDateTime());
        account.setModifiedAccountId(entity.getModifiedAccountId());
        account.setModifiedDateTime(entity.getModifiedDateTime());
        return account;
    }

}
