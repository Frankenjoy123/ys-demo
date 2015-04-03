package com.yunsoo.api.dto.basic;

import com.yunsoo.api.object.TAccountStatusEnum;

import java.util.Set;

/**
 * Created by Zhe on 2015/3/3.
 */
public class Account {
    private Long id;
    private TAccountStatusEnum status;
    private String ssid;
    private String identifier;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;

    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return identifier;
    }
    public boolean isAccountNonExpired() {
        return !(status == TAccountStatusEnum.EXPIRED);
    }
    public boolean isAccountNonLocked() {
        return !(status == TAccountStatusEnum.LOCKED);
    }
    public boolean isCredentialsNonExpired() {
        return !(status == TAccountStatusEnum.TOKEN_EXPIRED);
    }
    public boolean isEnabled() {
        return status == TAccountStatusEnum.ENABLED;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public TAccountStatusEnum getStatus() { return status; }
    public void setStatus(int status) { this.status = TAccountStatusEnum.valueOf(status); }
    public String getSsid() { return ssid; }
    public void setSsid(String ssid) { this.ssid = ssid; }
    public void setUsername(String identifier) { this.identifier = identifier; }
    public String getName() { return firstName + " " + lastName; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPassword(String password) { this.password = password; }
}
