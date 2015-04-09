package com.yunsoo.dataapi.dto;

import com.yunsoo.service.contract.Account;
import com.yunsoo.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;

/**
 * Created by Zhe on 2015/3/3.
 */
public class AccountDto {
    private long id;
    private Integer status;
    private String SSID;
    private String identifier;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String salt;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getSSID() { return SSID; }
    public void setSSID(String SSID) { this.SSID = SSID; }
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public static Account ToAccount(AccountDto dto) {
        Account account = new Account();
        BeanUtils.copyProperties(dto, account, SpringBeanUtil.getNullPropertyNames(dto));
        return account;
    }

    //convert User into UserDto
    public static AccountDto FromAccount(Account account) {
        if (account == null) return null;
        AccountDto dto = new AccountDto();
        BeanUtils.copyProperties(account, dto, SpringBeanUtil.getNullPropertyNames(account));
        return dto;
    }
}
