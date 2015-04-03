package com.yunsoo.dbmodel;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * KB on 3/8/2015
 */
@Entity
@Table(name = "account_org")
@XmlRootElement
@DynamicUpdate
public class AccountOrgModel {
    private Long id;
    private Integer status;
    private AccountModel account;
    private OrganizationModel org;
    private String employeeId;
    private Long roleId;
    @Id
    @GeneratedValue
    @Column(name = "id")
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Column(name = "status")
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    @Column(name = "employee_id", nullable = true)
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    @Column(name = "role_id", nullable = true)
    public Long getRole() { return roleId; }
    public void setRole(Long roleId) { this.roleId = roleId; }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    public AccountModel getAccount() { return account; }
    public void setAccount(AccountModel account) { this.account = account; }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    public OrganizationModel getOrg() {
        return org;
    }
    public void setOrg(OrganizationModel org) {
        this.org = org;
    }



}