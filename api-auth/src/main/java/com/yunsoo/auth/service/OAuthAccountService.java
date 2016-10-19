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

    public List<OAuthAccount> search(List<String> sourceList, String sourceType){
        List<OAuthAccountEntity> entities = repository.findBySourceInAndSourceTypeCodeAndDisabled(sourceList, sourceType, false);
        return entities.stream().map(OAuthAccount::new).collect(Collectors.toList());
    }

    public int count(List<String> sourceList, String sourceType) {
        return repository.countBySourceInAndSourceTypeCodeAndDisabled(sourceList, sourceType, false);
    }
}
