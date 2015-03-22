package com.yunsoo.api.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.api.dto.basic.Account;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * TAccount represents API user who consumes Yunsoo API.
 * Created by Zhe on 2015/3/5.
 */
public class TAccount implements UserDetails {
    private Long id;
    private TAccountStatusEnum status;
    private String ssid;
    private String identifier;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;

    private Set<TAccountAuthority> authorities;

    public TAccount() {}
    public TAccount(String identifier) {

    }

    @Override
    public Set<TAccountAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return identifier;
    }
    @Override
    public boolean isAccountNonExpired() {
        return !(status == TAccountStatusEnum.EXPIRED);
    }
    @Override
    public boolean isAccountNonLocked() {
        return !(status == TAccountStatusEnum.LOCKED);
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return !(status == TAccountStatusEnum.TOKEN_EXPIRED);
    }
    @Override
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

    public Account ToDto(TAccount account) {
        Account dto = new Account();
        BeanUtils.copyProperties(account, dto, SpringBeanUtil.getNullPropertyNames(account));
        return dto;
    }
}