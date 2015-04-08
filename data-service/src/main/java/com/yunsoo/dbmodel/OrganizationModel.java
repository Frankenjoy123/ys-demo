package com.yunsoo.dbmodel;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Zhe on 2015/2/5.
 */
@Entity
@Table(name = "organization")
public class OrganizationModel {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String detail;
    private int type;
    private int status;

    private Set<AccountOrgModel> accountOrgs;

    @javax.persistence.Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "detail")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Column(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "org")
    public Set<AccountOrgModel> getAccountOrgs() {
        return accountOrgs;
    }

    public void setAccountOrgs(Set<AccountOrgModel> accountOrgs) {
        this.accountOrgs = accountOrgs;
    }

}
