package com.yunsoo.data.service.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by:   Zhe
 * Created on:   2015/6/15
 * Descriptions:
 */
@Entity
@Table(name = "application")
public class ApplicationEntity {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.yunsoo.data.service.util.IdGenerator")
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "version")
    private String version;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "type_code")
    private String typeCode;

    @Column(name = "description")
    private String description;

    @Column(name = "system_version")
    private String systemVersion;

    @Column(name = "created_account_id")
    private String createdAccountId;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "modified_account_id")
    private String modifiedAccountId;

    @Column(name = "modified_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modifiedDateTime;


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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(String createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getModifiedAccountId() {
        return modifiedAccountId;
    }

    public void setModifiedAccountId(String modifiedAccountId) {
        this.modifiedAccountId = modifiedAccountId;
    }

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }
}
