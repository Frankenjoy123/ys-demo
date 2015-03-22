package com.yunsoo.service.contract;


import com.yunsoo.dbmodel.AccountTokenModel;
import org.joda.time.DateTime;

/**
 * Created by KB
 * Updated by Jerry
 */
public class AccountToken {
    private Long id;
    private Integer status;
    private Account account;
    private Long deviceId;
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

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long device) { this.deviceId = deviceId; }

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
        accountToken.setAccount(Account.FromModel(model.getAccount()));
        accountToken.setDeviceId(model.getDeviceId());
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
        model.setAccount(Account.ToModel(accountToken.getAccount()));
        model.setDeviceId(accountToken.getDeviceId());
        model.setAccessToken(accountToken.getAccessToken());
        model.setAccessTokenExpires(accountToken.getAccessTokenExpires());
        model.setAccessTokenTs(accountToken.getAccessTokenTs());
        model.setRefreshToken(accountToken.getRefreshToken());
        model.setRefreshTokenTs(accountToken.getRefreshTokenTs());
        model.setRefreshTokenExpires(accountToken.getRefreshTokenExpires());

        return model;
    }
}
