package com.yunsoo.api.rabbit.dto.basic;

import com.yunsoo.api.rabbit.object.TAccountStatusEnum;

/**
 * Created by Zhe on 2015/3/3.
 */
public class Account {
    private Long id;
    private String username;
    private String password;
    private TAccountStatusEnum status;
//    private String ssid;
//    private String identifier;
//    private String firstName;
//    private String lastName;
//    private String email;
//    private String phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TAccountStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TAccountStatusEnum status) {
        this.status = status;
    }

}
