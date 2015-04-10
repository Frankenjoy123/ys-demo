package com.yunsoo.data.service.service.contract;


import com.yunsoo.data.service.dbmodel.AccountModel;
import com.yunsoo.data.service.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;

/**
 * Created by KB
 */
public class Account {
    private Long id;
    private Integer status;
    private String SSID;
    private String identifier;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String salt;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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

    public static AccountModel ToModel(Account account) {
        if (account == null) return null;
        AccountModel model = new AccountModel();
        BeanUtils.copyProperties(account, model, SpringBeanUtil.getNullPropertyNames(account));
        return model;
    }
    public static Account FromModel(AccountModel model) {
        if (model == null) { return null; }
        Account account = new Account();
        BeanUtils.copyProperties(model, account, SpringBeanUtil.getNullPropertyNames(model));
        return account;
    }
}
