package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ElastiCacheConfig;
import com.yunsoo.common.data.CacheType;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.util.HashUtils;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * Created by  : Zhe
 * Created on  : 2015/4/19
 * Descriptions:
 */
@Component
@ElastiCacheConfig
public class AccountDomain {

    @Autowired
    private RestClient dataAPIClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDomain.class);

    @Cacheable(key="T(com.yunsoo.api.cache.CustomKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).ACCOUNT.toString(), #accountId)")
    public AccountObject getById(String accountId) {
        LOGGER.debug("missed cache: account." + accountId);
        if (StringUtils.isEmpty(accountId)) {
            return null;
        }
        try {
            return dataAPIClient.get("account/{id}", AccountObject.class, accountId);
        } catch (NotFoundException ex) {
            return null;
        }


    }

    public AccountObject getByOrgIdAndIdentifier(String orgId, String identifier) {
        AccountObject[] accountObjects = dataAPIClient.get("account?org_id={0}&identifier={1}", AccountObject[].class, orgId, identifier);
        if (accountObjects == null || accountObjects.length <= 0) {
            return null;
        } else if (accountObjects.length > 1) {
            throw new InternalServerErrorException("duplicated account found by [org_id: " + orgId + ", identifier: " + identifier + "]");
        }
        return accountObjects[0];
    }

    public Page<AccountObject> getByOrgId(String orgId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("account/pageable" + query, new ParameterizedTypeReference<List<AccountObject>>() {
        });
    }

    public Long count(String orgId, List<String> statusCodeIn) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("status_code_in", statusCodeIn)
                .build();
        return dataAPIClient.get("account/count" + query, Long.class);
    }

    public AccountObject createAccount(AccountObject accountObject) {
        accountObject.setId(null);
        accountObject.setCreatedDateTime(DateTime.now());
        accountObject.setStatusCode(LookupCodes.AccountStatus.AVAILABLE);
        String hashSalt = RandomUtils.generateString(8);
        String password = hashPassword(accountObject.getPassword(), hashSalt);
        accountObject.setHashSalt(hashSalt);
        accountObject.setPassword(password);

        return dataAPIClient.post("account", accountObject, AccountObject.class);
    }

    @CacheEvict(key="T(com.yunsoo.api.cache.CustomKeyGenerator).getKey(T(com.yunsoo.common.data.CacheType).ACCOUNT.toString(), #accountId)")
    public void updatePassword(String accountId, String rawNewPassword) {
        if (!StringUtils.isEmpty(accountId)) {
            String hashSalt = RandomUtils.generateString(8);
            String password = hashPassword(rawNewPassword, hashSalt);
            AccountObject accountObject = new AccountObject();
            accountObject.setPassword(password);
            accountObject.setHashSalt(hashSalt);
            dataAPIClient.patch("account/{id}", accountObject, accountId);
        }
    }

    public boolean isActiveAccount(AccountObject accountObject) {
        String statusCode = accountObject.getStatusCode();
        return LookupCodes.AccountStatus.AVAILABLE.equals(statusCode);
    }

    public boolean validatePassword(String rawPassword, String hashSalt, String password) {
        return rawPassword != null && hashSalt != null && hashPassword(rawPassword, hashSalt).equals(password);
    }


    private String hashPassword(String rawPassword, String hashSalt) {
        return HashUtils.sha1HexString(rawPassword + hashSalt);
    }


}
