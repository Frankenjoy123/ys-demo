package com.yunsoo.data.service.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Admin on 7/19/2016.
 */
@Entity
@Table(name = "web_template")
@IdClass(WebTemplateEntity.WebTemplatePK.class)
public class WebTemplateEntity {

    @Id
    @Column(name = "name")
    private String name;

    @Id
    @Column(name = "version")
    private String version;

    @Column(name = "description")
    private String description;

    @Column(name = "type_code")
    private String typeCode;

    @Column(name = "restriction")
    private String restriction;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public static class WebTemplatePK implements Serializable {

        private String name;

        private String version;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public WebTemplatePK() {
        }

        public WebTemplatePK(String name, String version) {
            this.name = name;
            this.version = version;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || this.name == null || this.version == null || !(object instanceof WebTemplatePK)) {
                return false;
            }

            WebTemplatePK obj = (WebTemplatePK) object;

            return this.name.equals(obj.name) && this.version.equals(obj.version);
        }

        @Override
        public int hashCode() {
            return 31 * name.hashCode() + version.hashCode();
        }

    }
}
