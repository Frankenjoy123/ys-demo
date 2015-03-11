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
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "account_org_id")
    private long accountOrgId;

    @Column(name = "permission_group1", nullable = true)
    private long permissionGroup1;

    @Column(name = "permission_group2", nullable = true)
    private long permissionGroup2;

    @Column(name = "created_ts", updatable = false, nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdTs;

    @Column(name = "updated_ts", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime updatedTs;

    @Column(name = "updated_by", nullable = true)
    private long updatedBy;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getAccountOrg() { return accountOrgId; }
    public void setAccountOrg(long accountOrgId) { this.accountOrgId = accountOrgId; }

    public long getPermissionGroup1() { return permissionGroup1; }
    public void setPermissionGroup1(long permissionGroup1) { this.permissionGroup1 = permissionGroup1; }

    public long getPermissionGroup2() { return permissionGroup2; }
    public void setPermissionGroup2(long permissionGroup2) { this.permissionGroup2 = permissionGroup2; }

    public DateTime getCreatedTs() { return createdTs; }
    public void setCreatedTs(DateTime createdTs) { this.createdTs = createdTs; }

    public DateTime getUpdatedTs() { return updatedTs; }
    public void setUpdatedTs(DateTime updatedTs) { this.updatedTs = updatedTs; }

    public long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(long updatedBy) { this.updatedBy = updatedBy; }
}