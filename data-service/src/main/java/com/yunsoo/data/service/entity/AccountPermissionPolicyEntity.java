package com.yunsoo.data.service.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by:   Lijian
 * Created on:   2015/4/13
 * Descriptions:
 */
@Entity
@Table(name = "account_permission_policy")
public class AccountPermissionPolicyEntity {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.yunsoo.data.service.util.IdGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "policy_code")
    private String policyCode;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDatetime;

    @Column(name = "created_account_id")
    private String createdAccountId;


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

    public DateTime getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(DateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public String getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(String createdAccountId) {
        this.createdAccountId = createdAccountId;
    }
}
