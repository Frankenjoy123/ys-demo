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
@Table(name = "org_role")
@XmlRootElement
@DynamicUpdate
public class OrgRoleModel {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "role")
    private String role;

    @ManyToOne
    @JoinColumn(name="org_id")
    private OrganizationModel org;

    public long getId() { return id; }
    public void setId(long id) { this.id= id; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public OrganizationModel getOrg() { return org; }
    public void setOrg(OrganizationModel org) { this.org = org; }

}