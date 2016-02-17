package com.yunsoo.data.service.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by:   yan
 * Created on:   9/7/2015
 * Descriptions:
 */
@Entity
@Table(name = "lookup_code")
@IdClass(LookupEntity.LookupPK.class)
public class LookupEntity {

    @Id
    @Column(name = "type_code")
    private String typeCode;

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private boolean active;


    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.typeCode == null || this.code == null || !(obj instanceof LookupEntity)) {
            return false;
        }

        LookupEntity entity = (LookupEntity) obj;

        return this.typeCode.equals(entity.typeCode) && this.code.equals(entity.code);
    }

    @Override
    public int hashCode() {
        return 31 * typeCode.hashCode() + code.hashCode();
    }

    public static class LookupPK implements Serializable {

        private String typeCode;

        private String code;

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public LookupPK() {
        }

        public LookupPK(String typeCode, String code) {
            this.typeCode = typeCode;
            this.code = code;
        }
    }

}
