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
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "status")
    private Integer status;

    @Column(name = "account_id")
    private long accountId;

    @Column(name = "org_id")
    private long orgId;

    @Column(name = "employee_id", nullable = true)
    private String employeeId;

    @Column(name = "role_id", nullable = true)
    private long roleId;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public long getAccount() { return accountId; }
    public void setAccount(long accountId) { this.accountId = accountId; }

    public long getOrg() { return orgId; }
    public void setOrg(long orgId) { this.orgId = orgId; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public long getRole() { return roleId; }
    public void setRole(long roleId) { this.roleId = roleId; }
}