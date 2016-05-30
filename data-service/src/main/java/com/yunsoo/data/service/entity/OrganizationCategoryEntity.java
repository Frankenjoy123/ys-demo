package com.yunsoo.data.service.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/1
 * Descriptions:
 */
@Entity
@Table(name = "org_category")
public class OrganizationCategoryEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.yunsoo.data.service.util.IdGenerator")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "active")
    private boolean active;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrganizationCategoryEntity)) return false;

        OrganizationCategoryEntity entity = (OrganizationCategoryEntity) o;

        if (active != entity.active) return false;
        if (!id.equals(entity.id)) return false;
        if (!name.equals(entity.name)) return false;
        if ((description!=null && !description.equals(entity.description))
                || (description == null && entity.description != null)) return false;
        return orgId.equals(entity.orgId);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + orgId.hashCode();
        result = 31 * result + (active ? 1 : 0);
        return result;
    }
}
