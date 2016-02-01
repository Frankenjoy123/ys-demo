package com.yunsoo.web.taobao.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 1/20/2016.
 */
public class TokenDomain {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expire_time")
    private long expireTime;
    @JsonProperty("expires_in")
    private long expiresIn;
    @JsonProperty("r1_expires_in")
    private long r1ExpiresIn;
    @JsonProperty("r1_valid")
    private long r1Valid;
    @JsonProperty("r2_expires_in")
    private long r2ExpiresIn;
    @JsonProperty("r2_valid")
    private long r2Valid;
    @JsonProperty("re_expires_in")
    private long reExpiresIn;
    @JsonProperty("refresh_token")
    private String refreshToke;
    @JsonProperty("refresh_token_valid_time")
    private long refreshTokenValidTime;
    @JsonProperty("taobao_user_id")
    private String taobaoUserId;
    @JsonProperty("taobao_user_nick")
    private String taobaoUserNick;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("w1_expires_in")
    private long w1ExpiresIn;
    @JsonProperty("w1_valid")
    private long w1Valid;
    @JsonProperty("w2_expires_in")
    private long w2ExpiresIn;
    @JsonProperty("w2_valid")
    private long w2Valid;

    public long getW2Valid() {
        return w2Valid;
    }

    public void setW2Valid(long w2Valid) {
        this.w2Valid = w2Valid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public long getR1ExpiresIn() {
        return r1ExpiresIn;
    }

    public void setR1ExpiresIn(long r1ExpiresIn) {
        this.r1ExpiresIn = r1ExpiresIn;
    }

    public long getR1Valid() {
        return r1Valid;
    }

    public void setR1Valid(long r1Valid) {
        this.r1Valid = r1Valid;
    }

    public long getR2ExpiresIn() {
        return r2ExpiresIn;
    }

    public void setR2ExpiresIn(long r2ExpiresIn) {
        this.r2ExpiresIn = r2ExpiresIn;
    }

    public long getR2Valid() {
        return r2Valid;
    }

    public void setR2Valid(long r2Valid) {
        this.r2Valid = r2Valid;
    }

    public long getReExpiresIn() {
        return reExpiresIn;
    }

    public void setReExpiresIn(long reExpiresIn) {
        this.reExpiresIn = reExpiresIn;
    }

    public String getRefreshToke() {
        return refreshToke;
    }

    public void setRefreshToke(String refreshToke) {
        this.refreshToke = refreshToke;
    }

    public long getRefreshTokenValidTime() {
        return refreshTokenValidTime;
    }

    public void setRefreshTokenValidTime(long refreshTokenValidTime) {
        this.refreshTokenValidTime = refreshTokenValidTime;
    }

    public String getTaobaoUserId() {
        return taobaoUserId;
    }

    public void setTaobaoUserId(String taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    public String getTaobaoUserNick() {
        return taobaoUserNick;
    }

    public void setTaobaoUserNick(String taobaoUserNick) {
        this.taobaoUserNick = taobaoUserNick;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getW1ExpiresIn() {
        return w1ExpiresIn;
    }

    public void setW1ExpiresIn(long w1ExpiresIn) {
        this.w1ExpiresIn = w1ExpiresIn;
    }

    public long getW1Valid() {
        return w1Valid;
    }

    public void setW1Valid(long w1Valid) {
        this.w1Valid = w1Valid;
    }

    public long getW2ExpiresIn() {
        return w2ExpiresIn;
    }

    public void setW2ExpiresIn(long w2ExpiresIn) {
        this.w2ExpiresIn = w2ExpiresIn;
    }
}
