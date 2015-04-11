package com.yunsoo.api.rabbit.object;

import com.yunsoo.api.rabbit.dto.basic.Account;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

/**
 * TAccount represents API user who consumes Yunsoo API.
 * Created by Zhe on 2015/3/5.
 */
public class TAccount implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private TAccountStatusEnum status;
    private Long expires;
    private Set<TAccountAuthority> authorities;

    public TAccount() {
    }

    public TAccount(TAccountStatusEnum status) {
        this.setStatus(status.value());
    }

    @Override
    public Set<TAccountAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isDefined() {
        return !(status == TAccountStatusEnum.UNDEFINED);
    }

    public boolean isAnonymous() {
        return !(status == TAccountStatusEnum.ANONYMOUS);
    }

    public boolean isCredentialsInvalid() {
        return !(status == TAccountStatusEnum.INVALID_TOKEN);
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TAccountStatusEnum getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = TAccountStatusEnum.valueOf(status);
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expired) {
        this.expires = expired;
    }

    public Account ToDto(TAccount account) {
        Account dto = new Account();
        BeanUtils.copyProperties(account, dto);
        return dto;
    }

    public TAccount FromDto(Account accountDto) {
        TAccount account = new TAccount();
        BeanUtils.copyProperties(accountDto, account);
        return account;
    }
}