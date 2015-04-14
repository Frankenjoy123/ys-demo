package com.yunsoo.data.service.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by:   Lijian
 * Created on:   2015/4/12
 * Descriptions:
 */
@Entity
@Table(name = "account_permission")
public class AccountPermissionEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "resource_code")
    private String resourceCode;

    @Column(name = "action_code")
    private String actionCode;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDatetime;

    @Column(name = "created_by")
    private Long createdBy;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public DateTime getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(DateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
