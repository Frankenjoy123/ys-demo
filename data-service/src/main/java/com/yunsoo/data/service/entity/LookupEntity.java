package com.yunsoo.data.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by yan on 9/7/2015.
 */
@Entity
@Table(name = "lookup_code")
public class LookupEntity {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private boolean active;

    @Column(name = "type_code")
    private String typeCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LookupEntity)) return false;

        LookupEntity entity = (LookupEntity) o;

        if (!code.equals(entity.code)) return false;
        return typeCode.equals(entity.typeCode);

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + typeCode.hashCode();
        return result;
    }
}
