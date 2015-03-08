package com.yunsoo.dbmodel;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

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
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "status")
    private Integer status;

    @ManyToMany
    @JoinColumn(name="account_id")
    private AccountModel account;

    @ManyToMany
    @Column(name = "org_id")
    private OrganizationModel org;

    @Column(name = "employee_id", nullable = true)
    private String employeeId;

    @ManyToOne
    @Column(name = "role_id", nullable = true)
    private OrgRoleModel role;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public AccountModel getAccount() { return account; }
    public void setAccount(AccountModel account) { this.account = account; }

    public OrganizationModel getOrg() { return org; }
    public void setOrg(OrganizationModel org) { this.org = org; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public OrgRoleModel getRole() { return role; }
    public void setRole(OrgRoleModel role) { this.role = role; }
}