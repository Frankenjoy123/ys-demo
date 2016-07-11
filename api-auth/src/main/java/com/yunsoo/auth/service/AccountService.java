package com.yunsoo.auth.service;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.dao.entity.AccountEntity;
import com.yunsoo.auth.dao.repository.AccountRepository;
import com.yunsoo.auth.dto.Account;
import com.yunsoo.common.util.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-07-06
 * Descriptions:
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;


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

    //    public Page<Account> getByOrgId(String orgId, String statusCode, String searchText, DateTime start, DateTime end, Pageable pageable) {
//        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
//                .append("org_id", orgId).append("status", status).append("search_text", searchText)
//                .append("start_datetime", start).append("end_datetime", end)
//                .append(pageable)
//                .build();
//
//        return dataAPIClient.getPaged("account" + query, new ParameterizedTypeReference<List<AccountObject>>() {
//        });
//    }


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
        account.setEmail(entity.getEmail());
        account.setFirstName(entity.getFirstName());
        account.setLastName(entity.getLastName());
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
