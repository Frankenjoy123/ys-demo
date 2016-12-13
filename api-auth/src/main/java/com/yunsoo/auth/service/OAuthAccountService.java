package com.yunsoo.auth.service;

import com.yunsoo.auth.dao.entity.OAuthAccountEntity;
import com.yunsoo.auth.dao.repository.OAuthAccountRepository;
import com.yunsoo.auth.dto.OAuthAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-10-18
 * Descriptions:
 */
@Service
public class OAuthAccountService {

    @Autowired
    private OAuthAccountRepository repository;

    public List<OAuthAccount> search(List<String> sourceList, String sourceType, String accountId){
        boolean hasSource = true;
        if(sourceList == null)
            hasSource = false;

        List<OAuthAccountEntity> entities = repository.query(hasSource, sourceList, sourceType, accountId, false);
        return entities.stream().map(OAuthAccount::new).collect(Collectors.toList());
    }

    public int count(List<String> sourceList, String sourceType) {
        return repository.countBySourceInAndSourceTypeCodeAndDisabled(sourceList, sourceType, false);
    }

    public OAuthAccount getById(String id){
        OAuthAccountEntity entity = repository.findOne(id);
        return new OAuthAccount(entity);
    }

    public OAuthAccount save(OAuthAccount account){
        OAuthAccountEntity entity = new OAuthAccountEntity(account);
        repository.save(entity);
        return new OAuthAccount(entity);
    }

    public List<OAuthAccount> getByOAuthOpenIdAndOAuthTypeCode(String openId, String openType, String sourceType, String sourceId){
        List<OAuthAccountEntity> accounts = repository.findByOAuthTypeCodeAndOAuthOpenIdAndSourceAndSourceTypeCodeAndDisabled(openType, openId, sourceId, sourceType, false);
        return accounts.stream().map(OAuthAccount::new).collect(Collectors.toList());
    }

}
