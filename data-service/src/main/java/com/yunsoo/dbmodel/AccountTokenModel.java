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
    private Long id;
    private Integer status;
    private AccountModel account;
    private Long deviceId;
    private String accessToken;
    private DateTime accessTokenTs;
    private DateTime accessTokenExpires;
    private String refreshToken;
    private DateTime refreshTokenTs;
    private DateTime refreshTokenExpires;
    @Id
    @GeneratedValue
    @Column(name = "id")
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Column(name = "status", nullable = true)
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    @Column(name = "device_id", nullable = true)
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }

    @Column(name = "access_token", nullable = true)
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    @Column(name = "access_token_ts", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getAccessTokenTs() { return accessTokenTs; }
    public void setAccessTokenTs(DateTime accessTokenTs) { this.accessTokenTs = accessTokenTs; }

    @Column(name = "access_token_expires", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getAccessTokenExpires() { return accessTokenExpires; }
    public void setAccessTokenExpires(DateTime accessTokenExpires) { this.accessTokenExpires = accessTokenExpires; }

    @Column(name = "refresh_token", nullable = true)
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    @Column(name = "refresh_token_ts", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getRefreshTokenTs() { return refreshTokenTs; }
    public void setRefreshTokenTs(DateTime refreshTokenTs) { this.refreshTokenTs = refreshTokenTs; }

    @Column(name = "refresh_token_expires", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getRefreshTokenExpires() { return refreshTokenExpires; }
    public void setRefreshTokenExpires(DateTime refreshTokenExpires) { this.refreshTokenExpires = refreshTokenExpires; }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    public AccountModel getAccount() {
        return account;
    }

    public void setAccount(AccountModel account) {
        this.account = account;
    }

}
