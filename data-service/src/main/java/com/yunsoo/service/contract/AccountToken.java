package com.yunsoo.service.contract;


import com.yunsoo.dbmodel.AccountTokenModel;
import org.joda.time.DateTime;

/**
 * Created by KB
 * Updated by Jerry
 */
public class AccountToken {
    private long id;
    private Integer status;
    private Long account;
    private String device;
    private String accessToken;
    private DateTime accessTokenTs;
    private DateTime accessTokenExpires;
    private String refreshToken;
    private DateTime refreshTokenTs;
    private DateTime refreshTokenExpires;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Long getAccount() { return account; }
    public void setAccount(long account) { this.account = account; }

    public String getDevice() { return device; }
    public void setDevice(String device) { this.device = device; }

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

    public static AccountToken FromModel(AccountTokenModel model) {
        if (model == null) return null;
        AccountToken accountToken = new AccountToken();
        accountToken.setId(model.getId());
        accountToken.setStatus(model.getStatus());
        accountToken.setAccount(model.getAccount());
        accountToken.setDevice(model.getDevice());
        accountToken.setAccessToken(model.getAccessToken());
        accountToken.setAccessTokenExpires(model.getAccessTokenExpires());
        accountToken.setAccessTokenTs(model.getAccessTokenTs());
        accountToken.setRefreshToken(model.getRefreshToken());
        accountToken.setRefreshTokenTs(model.getRefreshTokenTs());
        accountToken.setRefreshTokenExpires(model.getRefreshTokenExpires());

        return accountToken;
    }

    public static AccountTokenModel ToModel(AccountToken accountToken) {
        if (accountToken == null) return null;
        AccountTokenModel model = new AccountTokenModel();
        if (accountToken.getId() >= 0) {
            model.setId(accountToken.getId());
        }
        model.setStatus(accountToken.getStatus());
        model.setAccount(accountToken.getAccount());
        model.setDevice(accountToken.getDevice());
        model.setAccessToken(accountToken.getAccessToken());
        model.setAccessTokenExpires(accountToken.getAccessTokenExpires());
        model.setAccessTokenTs(accountToken.getAccessTokenTs());
        model.setRefreshToken(accountToken.getRefreshToken());
        model.setRefreshTokenTs(accountToken.getRefreshTokenTs());
        model.setRefreshTokenExpires(accountToken.getRefreshTokenExpires());

        return model;
    }
}
