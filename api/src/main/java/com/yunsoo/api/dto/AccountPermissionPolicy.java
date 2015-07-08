package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.AccountPermissionPolicyObject;
import org.joda.time.DateTime;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/18
 * Descriptions:
 */
public class AccountPermissionPolicy {

    @JsonProperty("id")
    private String id;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("policy_code")
    private String policyCode;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }


    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public String getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(String createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }


    public AccountPermissionPolicy() {
    }

    public AccountPermissionPolicy(AccountPermissionPolicyObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setAccountId(object.getAccountId());
            this.setOrgId(object.getOrgId());
            this.setPolicyCode(object.getPolicyCode());
            this.setCreatedAccountId(object.getCreatedAccountId());
            this.setCreatedDateTime(object.getCreatedDatetime());
        }
    }

    public AccountPermissionPolicyObject toAccountPermissionPolicyObject() {
        AccountPermissionPolicyObject object = new AccountPermissionPolicyObject();
        object.setId(this.getId());
        object.setAccountId(this.getAccountId());
        object.setOrgId(this.getOrgId());
        object.setPolicyCode(this.getPolicyCode());
        object.setCreatedAccountId(this.getCreatedAccountId());
        object.setCreatedDatetime(this.getCreatedDateTime());
        return object;
    }
}
