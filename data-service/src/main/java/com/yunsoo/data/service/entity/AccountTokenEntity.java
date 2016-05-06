package com.yunsoo.data.service.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/20
 * Descriptions:
 */
@Entity
@Table(name = "account_token")
public class AccountTokenEntity {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.yunsoo.data.service.util.IdGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "permanent_token")
    private String permanentToken;

    @Column(name = "permanent_token_expires_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime permanentTokenExpiresDateTime;

    @Column(name = "created_account_id")
    private String createdAccountId;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPermanentToken() {
        return permanentToken;
    }

    public void setPermanentToken(String permanentToken) {
        this.permanentToken = permanentToken;
    }

    public DateTime getPermanentTokenExpiresDateTime() {
        return permanentTokenExpiresDateTime;
    }

    public void setPermanentTokenExpiresDateTime(DateTime permanentTokenExpiresDateTime) {
        this.permanentTokenExpiresDateTime = permanentTokenExpiresDateTime;
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
}
