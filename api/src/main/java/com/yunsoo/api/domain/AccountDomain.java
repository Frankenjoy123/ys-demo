package com.yunsoo.api.domain;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.AccountGroupObject;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.data.object.GroupObject;
import com.yunsoo.common.util.HashUtils;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Zhe
 * Created on  : 2015/4/19
 * Descriptions:
 */
@Component
public class AccountDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private GroupDomain groupDomain;


    public AccountObject getById(String accountId) {
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

    public Page<List<AccountObject>> getByOrgId(String orgId, Pageable pageable) {

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
        accountObject.setStatusCode(LookupCodes.AccountStatus.CREATED);
        String hashSalt = RandomUtils.generateString(8);
        String password = hashPassword(accountObject.getPassword(), hashSalt);
        accountObject.setHashSalt(hashSalt);
        accountObject.setPassword(password);

        return dataAPIClient.post("account", accountObject, AccountObject.class);
    }

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


    //group
    public List<GroupObject> getGroups(AccountObject accountObject) {
        List<AccountGroupObject> accountGroupObjects = dataAPIClient.get("accountgroup?account_id={accountId}", new ParameterizedTypeReference<List<AccountGroupObject>>() {
        }, accountObject.getId());
        if (accountGroupObjects.size() == 0) {
            return new ArrayList<>();
        }
        List<GroupObject> allGroups = groupDomain.getByOrgId(accountObject.getOrgId());
        List<String> groupIds = accountGroupObjects.stream().map(AccountGroupObject::getGroupId).collect(Collectors.toList());
        return allGroups.stream().filter(g -> groupIds.contains(g.getId())).collect(Collectors.toList());
    }


    private String hashPassword(String rawPassword, String hashSalt) {
        return HashUtils.sha1HexString(rawPassword + hashSalt);
    }


}
