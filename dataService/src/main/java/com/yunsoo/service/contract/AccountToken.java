package com.yunsoo.service.contract;


import org.joda.time.DateTime;

/**
 * Created by KB
 */
public class AccountToken {
    private long id;
    private Integer status;
    private Account account;
    private Device device;
    private String accessToken;
    private DateTime accessTokenTs;
    private DateTime accessTokenExpires;
    private String refreshToken;
    private DateTime refreshTokenTs;
    private DateTime refreshTokenExpires;
}
