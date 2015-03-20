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
@Table(name = "account_permission")
@XmlRootElement
@DynamicUpdate
public class AccountPermissionModel {
    private Long id;
    private Long accountOrgId;
    private Long permissionGroup1;
    private Long permissionGroup2;
    private DateTime createdTs;
    private DateTime updatedTs;
    private Long updatedBy;
    @Id
    @GeneratedValue
    @Column(name = "id")
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Column(name = "account_org_id")
    public Long getAccountOrg() { return accountOrgId; }
    public void setAccountOrg(Long accountOrgId) { this.accountOrgId = accountOrgId; }

    @Column(name = "permission_group1", nullable = true)
    public Long getPermissionGroup1() { return permissionGroup1; }
    public void setPermissionGroup1(Long permissionGroup1) { this.permissionGroup1 = permissionGroup1; }

    @Column(name = "permission_group2", nullable = true)
    public Long getPermissionGroup2() { return permissionGroup2; }
    public void setPermissionGroup2(Long permissionGroup2) { this.permissionGroup2 = permissionGroup2; }

    @Column(name = "created_ts", updatable = false, nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getCreatedTs() { return createdTs; }
    public void setCreatedTs(DateTime createdTs) { this.createdTs = createdTs; }

    @Column(name = "updated_ts", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getUpdatedTs() { return updatedTs; }
    public void setUpdatedTs(DateTime updatedTs) { this.updatedTs = updatedTs; }

    @Column(name = "updated_by", nullable = true)
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }

}