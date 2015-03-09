package com.yunsoo.dbmodel;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * KB on 3/8/2015
 */
@Entity
@Table(name = "account_token")
@XmlRootElement
@DynamicUpdate
public class AccountTokenModel {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "status", nullable = true)
    private Integer status;

    @Column(name = "account_id", nullable = true)
    private long account;

    @Column(name = "device_id", nullable = true)
    private long device;

    @Column(name = "access_token", nullable = true)
    private String accessToken;

    @Column(name = "access_token_ts", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime accessTokenTs;

    @Column(name = "access_token_expires", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime accessTokenExpires;

    @Column(name = "refresh_token", nullable = true)
    private String refreshToken;

    @Column(name = "refresh_token_ts", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime refreshTokenTs;

    @Column(name = "refresh_token_expires", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime refreshTokenExpires;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public long getAccount() { return account; }
    public void setAccount(long account) { this.account = account; }

    public long getDevice() { return device; }
    public void setDevice(long device) { this.device = device; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public DateTime getAccessTokenTs() { return accessTokenTs; }
    public void setAccessTokenTs(DateTime accessTokenTs) { this.accessTokenTs = accessTokenTs; }

    public DateTime getAccessTokenExpires() { return accessTokenExpires; }
    public void setAccessTokenExpires(DateTime accessTokenExpires) { this.accessTokenExpires = accessTokenExpires; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public DateTime getRefreshTokenTs() { return refreshTokenTs; }
    public void setRefreshTokenTs(DateTime refreshTokenTs) { this.refreshTokenTs = refreshTokenTs; }

    public DateTime getRefreshTokenExpires() { return refreshTokenExpires; }
    public void setRefreshTokenExpires(DateTime refreshTokenExpires) { this.refreshTokenExpires = refreshTokenExpires; }

}
